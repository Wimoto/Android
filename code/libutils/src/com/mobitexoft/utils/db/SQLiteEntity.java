package com.mobitexoft.utils.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.mobitexoft.utils.db.SQLiteBase.DatabaseColumn;
import com.mobitexoft.utils.db.SQLiteBase.DatabaseEntity;
import com.mobitexoft.utils.propertyobserver.PropertyObservable;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * Ver.1.0
 */
public class SQLiteEntity extends PropertyObservable{
	
	protected long mId;
	
	public SQLiteEntity() {
		DatabaseEntity annotation = getClass().getAnnotation(DatabaseEntity.class);
		if (annotation == null) {
			throw new IllegalStateException("A DatabaseEntity annotation not set for this class");
		} else {
			if (TextUtils.isEmpty(annotation.tableName())) {
				throw new IllegalStateException("A <tableName> field in DatabaseEntity annotation is empty");
			}
		}
	}
	
	long getId() {
		return mId;
	}
	
	void setId(long id) {
		mId = id;
	}
	
	public String getRelatedTableName() {
		DatabaseEntity annotation = getClass().getAnnotation(DatabaseEntity.class);
		return annotation.tableName();
	}
	
	String getSQLiteTableCreateQuery() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE " + getRelatedTableName() + "(");
		builder.append("_id INTEGER PRIMARY KEY AUTOINCREMENT");
		for(Entry<String, Field> entry : getEntityFields().entrySet()) {
			builder.append(", " + entry.getKey() + " " + getSQLiteType(entry.getValue().getType()));
		}			
		builder.append(")");
		return builder.toString();
	}

	HashMap<String, Field> getEntityFields() {
		HashMap<String, Field> entityFields = new HashMap<String, Field>();
		ArrayList<String> fieldsNames = new ArrayList<String>();
		Class<?> cls = getClass();
		do {
			for (Field field : cls.getDeclaredFields()) {
				DatabaseColumn annotation = field.getAnnotation(DatabaseColumn.class);
				if (annotation != null) {
					if (TextUtils.isEmpty(annotation.columnName())) {
						throw new IllegalStateException("A DatabaseColumn annotation contains empty fields");
					}
					int modifiers = field.getModifiers();
					if (Modifier.isPrivate(modifiers)) {
						String text = String.format("A DatabaseColumn annotation can't be set to private field <%s>",
								field.getName());
						throw new IllegalStateException(text);
					}
					if (Modifier.isFinal(modifiers)) {
						String text = String.format("A DatabaseColumn annotation can't be set to final field <%s>",
								field.getName());
						throw new IllegalStateException(text);
					}						
					if (entityFields.containsKey(annotation.columnName())) {
						String text = String.format("A column name <%s> from DatabaseColumn annotation already use in other field",
								annotation.columnName());
						throw new IllegalStateException(text);
					}						
					if (fieldsNames.contains(field.getName())) {
						String text = String.format("Using of overlapping fields <%s> is denied", field.getName());
						throw new IllegalStateException(text);
					} else {
						fieldsNames.add(field.getName());
					}
					entityFields.put(annotation.columnName(), field);
				}
			}
		} while ((cls = cls.getSuperclass()) != null);
		return entityFields;
	}
	
	String getSQLiteType(Class<?> type) {
		if (type.isPrimitive()) {
			if (type.equals(float.class) || type.equals(double.class)) {
				return "REAL";
			} else {
				return "INTEGER";
			}
		} else if (String.class.isAssignableFrom(type)) {
			return "TEXT";
		} else if (byte[].class.isAssignableFrom(type)) {
			return "BLOB";
		}	else if (SQLiteEntity.class.isAssignableFrom(type)) {
			return "INTEGER";
		} else {
			String text = String.format("Unsupported field type <%s>", type.getName());
			throw new IllegalStateException(text);
		}
	}
	
	ContentValues getSQLiteContentValues() throws IllegalAccessException, IllegalArgumentException {
		ContentValues values = new ContentValues();
		for(Entry<String, Field> entry : getEntityFields().entrySet()) {
			if (!entry.getValue().isAccessible()) {
				entry.getValue().setAccessible(true);
			}
			if (boolean.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getBoolean(this));
			} else if (byte.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getByte(this));
			} else if (short.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getShort(this));
			} else if (int.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getInt(this));
			} else if (long.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getLong(this));
			} else if (float.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getFloat(this));
			} else if (double.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), entry.getValue().getDouble(this));
			} else if (String.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), (String)entry.getValue().get(this));
			} else if (byte[].class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), (byte[])entry.getValue().get(this));
			} else if (SQLiteEntity.class.isAssignableFrom(entry.getValue().getType())) {
				values.put(entry.getKey(), 
					entry.getValue().get(this) == null ? 0 : ((SQLiteEntity)entry.getValue().get(this)).getId());
			} else {
				String text = String.format("Unsupported field type <%s>", entry.getValue().getType().getName());
				throw new IllegalStateException(text);
			}
		}
		return values;
	}
	
	void setSQLiteCursor(Cursor cursor) throws IllegalAccessException, IllegalArgumentException {
		if (cursor != null && !cursor.isAfterLast()) {
			mId = cursor.getLong(cursor.getColumnIndex("_id"));
			HashMap<String, Field> entityFields = getEntityFields();
			for (String columnName : cursor.getColumnNames()) {
				if (entityFields.containsKey(columnName)) {
					if (!entityFields.get(columnName).isAccessible()) {
						entityFields.get(columnName).setAccessible(true);
					}
					if (boolean.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setBoolean(this, cursor.getLong(cursor.getColumnIndex(columnName)) > 0);
					} else if (byte.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setByte(this, (byte)cursor.getLong(cursor.getColumnIndex(columnName)));
					} else if (short.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setShort(this, (short)cursor.getLong(cursor.getColumnIndex(columnName)));
					} else if (int.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setInt(this, cursor.getInt(cursor.getColumnIndex(columnName)));
					} else if (long.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setLong(this, cursor.getLong(cursor.getColumnIndex(columnName)));
					} else if (float.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setFloat(this, cursor.getFloat(cursor.getColumnIndex(columnName)));
					} else if (double.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).setDouble(this, cursor.getDouble(cursor.getColumnIndex(columnName)));
					} else if (String.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).set(this, cursor.getString(cursor.getColumnIndex(columnName)));
					} else if (byte[].class.isAssignableFrom(entityFields.get(columnName).getType())) {
						entityFields.get(columnName).set(this, cursor.getBlob(cursor.getColumnIndex(columnName)));
					} else if (SQLiteEntity.class.isAssignableFrom(entityFields.get(columnName).getType())) {
						if (entityFields.get(columnName).get(this) != null) {
							((SQLiteEntity)entityFields.get(columnName).get(this)).setId(cursor.getLong(cursor.getColumnIndex(columnName)));
						}
					} else {
						String text = String.format("Unsupported field type <%s>", entityFields.get(columnName).getType().getName());
						throw new IllegalStateException(text);
					}
				}
			}
		} else {
			throw new IllegalStateException("Cursor is null or a marker of position is after last");
		}
	}
}
