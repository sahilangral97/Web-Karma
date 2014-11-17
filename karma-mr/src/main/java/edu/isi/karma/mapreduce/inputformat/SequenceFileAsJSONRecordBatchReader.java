package edu.isi.karma.mapreduce.inputformat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileRecordReader;
import org.json.JSONArray;
import org.json.JSONObject;

@InterfaceAudience.Public
@InterfaceStability.Stable
public class SequenceFileAsJSONRecordBatchReader
extends RecordReader<Text, Text> {

	private final SequenceFileRecordReader<WritableComparable<?>, Writable>
	sequenceFileRecordReader;

	List<JSONObject> data = new LinkedList<JSONObject>();
	private static final int batchSize = 10000;
	public SequenceFileAsJSONRecordBatchReader()
			throws IOException {
		sequenceFileRecordReader =
				new SequenceFileRecordReader<WritableComparable<?>, Writable>();
	}

	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		sequenceFileRecordReader.initialize(split, context);
	}

	@Override
	public Text getCurrentKey() 
			throws IOException, InterruptedException {
		return new Text("json");
	}

	@Override
	public Text getCurrentValue() 
			throws IOException, InterruptedException {
		JSONArray array = new JSONArray();
		for (JSONObject obj : data) {
			array.put(obj);
		}
		return new Text(array.toString());
	}

	public synchronized boolean nextKeyValue() 
			throws IOException, InterruptedException {
		int count = 0;
		data.clear();
		while (sequenceFileRecordReader.nextKeyValue()) {
			JSONObject obj = new JSONObject(sequenceFileRecordReader.getCurrentValue().toString());
			data.add(obj);
			count++;
			if (count == batchSize) {
				break;
			}
		}
		return (data.size() != 0);
	}

	public float getProgress() throws IOException,  InterruptedException {
		return sequenceFileRecordReader.getProgress();
	}

	public synchronized void close() throws IOException {
		sequenceFileRecordReader.close();
	}
}
