package com.mobitexoft.utils.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

/**
 * Ver.1.0
 */
public class SQLiteBase {

	@Inherited
	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = { ElementType.TYPE })
	public @interface DatabaseEntity {
		String tableName();		
	}
	
	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = { ElementType.FIELD })
	public @interface DatabaseColumn {
		String columnName();		
	}
	
	public interface SQLiteBaseCreator {
		void onCreate(SQLiteDatabase db);
		void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
		void onOpen(SQLiteDatabase db);
	}
	
	private Context mContext;
	private final String mName;
	private final int mVersion;
	private SQLiteDatabase mDatabase;
	private SQLiteBaseCreator mCreator;
	private ArrayList<String> mTablesList;
	
	public static SQLiteBase getInstance(Context context, String name, int version, SQLiteBaseCreator creator) {
		return new SQLiteBase(context, name, version, creator);
	}
	
	public static SQLiteBase getInstance(Context context, String name, int version, SQLiteBaseCreator creator, Class<? extends SQLiteEntity>[] compatibleEntities) {
		SQLiteBase base = new SQLiteBase(context, name, version, creator);
		for (Class<? extends SQLiteEntity> cls : compatibleEntities) {
			if (base.isCompatible(cls)) {
				base.addTableIfNeeded(cls);
			} else {
				base.disconnectSQLiteDatabase();
				throw new SQLiteBaseException(String.format("Class <%s> not compatible with <%s> database", cls.getName(), base.mName));
			}
		}
		return base;
	}
	
	private SQLiteBase(Context context, String name, int version, SQLiteBaseCreator creator) {
		if (version < 1) {
			throw new SQLiteBaseException("Version must be >= 1, was " + version);
		}
		mContext = context;
		mName = name;
		mVersion = version;
		mCreator = creator;
		try {
			connectSQLiteDatabase();
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		disconnectSQLiteDatabase();
		super.finalize();
	}
	
	private void connectSQLiteDatabase() throws Exception {
		if (mDatabase != null) {
			if (mDatabase.isOpen()) {
				return;
			}
			mDatabase.close();
		}
		boolean success = false;
		try {
			if (TextUtils.isEmpty(mName)) {
				mDatabase = SQLiteDatabase.create(null);
            } else {
            	mDatabase = mContext.openOrCreateDatabase(mName, 0, null);
            }
			int version = mDatabase.getVersion();
            if (version != mVersion) {
            	mDatabase.beginTransaction();
                try {
                	if (mCreator != null) {
                		if (version == 0) {
                			mCreator.onCreate(mDatabase);
	                    } else {
	                    	mCreator.onUpgrade(mDatabase, version, mVersion);
	                    }
                	}	                    
                	mDatabase.setVersion(mVersion);
                	mDatabase.setTransactionSuccessful();
                } finally {
                	mDatabase.endTransaction();
                }
            }
            if (mCreator != null) {
            	mCreator.onOpen(mDatabase);
            }
            mTablesList = getTablesList();
			success = true;
		} finally {
			if (!success) {
				mTablesList = null;
				if (mDatabase != null) {
					mDatabase.close();
					mDatabase = null;
				}
			}
		}
	}
	
	public void disconnectSQLiteDatabase() {
		if (mDatabase != null) {
			mDatabase.close();
        }
		mTablesList = null;
		mDatabase = null;
	}
	
	private ArrayList<String> getTablesList() {
		ArrayList<String> result = new ArrayList<String>();
		if (mDatabase == null) {
			throw new IllegalStateException(String.format("Open database <%s> first", mName));
		}
		Cursor cursor = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);
        if (cursor.moveToFirst()) {
            do {
            	result.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
	}
	
	private void addTableIfNeeded(Class<? extends SQLiteEntity> cls) {		
		try {
			SQLiteEntity entity = cls.newInstance();
			if (!isTableExists(entity.getRelatedTableName())) {
				mDatabase.execSQL(entity.getSQLiteTableCreateQuery());
				mTablesList = getTablesList();
			}
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		}
	}
	
	public boolean isTableExists(String tableName) {
		if (mTablesList == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		return mTablesList.contains(tableName);
	}
	
	public boolean isCompatible(Class<? extends SQLiteEntity> cls) {
		if (mDatabase == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		Cursor cursor = null;
		try {
			String tableName = cls.getAnnotation(DatabaseEntity.class).tableName();
			cursor = mDatabase.rawQuery("SELECT sql FROM sqlite_master WHERE type='table' AND name='" + tableName + "';", null);
			String sql = null;
			if (cursor != null && cursor.moveToFirst()) {
				sql = cursor.getString(0);
			}
			if (sql == null) {
				return true;
			}
			String createQuery = cls.newInstance().getSQLiteTableCreateQuery();
			return createQuery.equals(sql);
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	/**
	 * Use {@link #newEntity(Class)} method instead of this
	 * @param cls class of needed entity
	 * @return new entity related with database
	 */
	@Deprecated
	public SQLiteEntity getNewEntity(Class<? extends SQLiteEntity> cls) {
		return newEntity(cls);
	}
	
	/**
	 * Get instance of specified class which is related with database
	 * @param cls class of needed entity
	 * @return new entity related with database
	 */
	public SQLiteEntity newEntity(Class<? extends SQLiteEntity> cls) {
		if (mDatabase == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		try {
			SQLiteEntity entity = cls.newInstance();
			addTableIfNeeded(entity.getClass());
			long id = mDatabase.insert(entity.getRelatedTableName(), null, entity.getSQLiteContentValues());
			if (id > 0) {
				entity.setId(id);
			} else {
				throw new SQLiteException(String.format("Failed to insert row into <%s> table", entity.getRelatedTableName()));
			}
			return entity;
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		}
	}
	
	public void saveEntity(SQLiteEntity entity) {
		if (mDatabase == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		try {
			addTableIfNeeded(entity.getClass());
			if (entity.getId() > 0) {
				String whereClause = "_id = ?";
				String[] whereArgs = { String.valueOf(entity.getId()) };
				int count = mDatabase.update(entity.getRelatedTableName(), entity.getSQLiteContentValues(), whereClause, whereArgs);
				if (count == 0) {
					throw new SQLiteException(String.format("Failed to update row into <%s> table", entity.getRelatedTableName()));
				}
			} else {
				long id = mDatabase.insert(entity.getRelatedTableName(), null, entity.getSQLiteContentValues());
				if (id > 0) {
					entity.setId(id);
				} else {
					throw new SQLiteException(String.format("Failed to insert row into <%s> table", entity.getRelatedTableName()));
				}
			}
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		}
	}
	
	public ArrayList<SQLiteEntity> findEntities(Class<? extends SQLiteEntity> cls, String whereClause, String[] whereArgs) {
		if (mDatabase == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		Cursor cursor = null;
		try {
			ArrayList<SQLiteEntity> entities = new ArrayList<SQLiteEntity>();
			SQLiteEntity entity = cls.newInstance();
			if (isTableExists(entity.getRelatedTableName())) {
				cursor = mDatabase.query(entity.getRelatedTableName(), null, whereClause, whereArgs, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						entity = cls.newInstance();
						entity.setSQLiteCursor(cursor);
						entities.add(entity);
					} while (cursor.moveToNext());
				}
			}
			return entities;
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public SQLiteEntity findFirstEntity(Class<? extends SQLiteEntity> cls, String whereClause, String[] whereArgs) {
		ArrayList<SQLiteEntity> entities = findEntities(cls, whereClause, whereArgs);
		if (entities.size() > 0) {
			return entities.get(0);
		}
		return null;
	}
	
	public void deleteEntity(SQLiteEntity entity) {
		if (mDatabase == null) {
			throw new SQLiteBaseException(String.format("Open database <%s> first", mName));
		}
		try {
			if (entity.getId() > 0 && isTableExists(entity.getRelatedTableName())) {
				String whereClause = "_id = ?";
				String[] whereArgs = { String.valueOf(entity.getId()) };
				int count = mDatabase.delete(entity.getRelatedTableName(), whereClause, whereArgs);
				if (count == 0) {
					throw new SQLiteException(String.format("Failed to delete row into <%s> table", entity.getRelatedTableName()));
				}
			}
		} catch (Exception e) {
			throw new SQLiteBaseException(e);
		}
	}
	
	public void clean() {
		ArrayList<String> tables = getTablesList();
		for (String tableName : tables) {
			try {
				mDatabase.beginTransaction();
				mDatabase.delete(tableName, null, null);
				mDatabase.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
		    	mDatabase.endTransaction();
			}
		}
	}
}
