package com.mobitexoft.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class SequenceInputStream extends InputStream {

	private java.io.SequenceInputStream mSequenceInputStream;
	
	private Vector<InputStream> mInputStreams = new Vector<InputStream>();
	
	@Override
	public int read() throws IOException {
		if (mSequenceInputStream == null) {
			mSequenceInputStream = new java.io.SequenceInputStream(mInputStreams.elements());
		}
		int bytes = mSequenceInputStream.read();
		if (bytes == -1) {
			mSequenceInputStream.close();
		}
		return bytes;
	}
	
	@Override
	public int read(byte[] buffer) throws IOException {
		if (mSequenceInputStream == null) {
			mSequenceInputStream = new java.io.SequenceInputStream(mInputStreams.elements());
		}
		int bytes = mSequenceInputStream.read(buffer);
		if (bytes == -1) {
			mSequenceInputStream.close();
		}
		return bytes;
	}
	
	private void add(InputStream inputStream, int location) {
		mInputStreams.add(location, inputStream);
	}
	
	public void addFirst(InputStream inputStream) {
		add(inputStream, 0);
	}
	
	public void addLast(InputStream inputStream) {
		add(inputStream, mInputStreams.size());
	}
	
	public void add(InputStream inputStream) {
		mInputStreams.add(inputStream);
	}

}
