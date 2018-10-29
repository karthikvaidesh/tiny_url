package ncsu.tinyurl.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/*
 * Main class of the TFIDF MapReduce implementation.
 * Author: Tyler Stocksdale
 * Date:   10/18/2017
 */
public class DataAggregator {

    public static void main(String[] args) throws Exception {
        // Check for correct usage
        if (args.length != 1) {
            System.err.println("Usage: Data Aggregator <input dir>");
            System.exit(1);
        }

		// Create configuration
		Configuration conf = new Configuration();

		// Input and output paths for each job
		Path inputPath = new Path(args[0]);
		Path wcInputPath = inputPath;
		Path wcOutputPath = new Path("data/output");

		// Get/set the number of documents (to be used in the data aggregator MapReduce job)
        FileSystem fs = inputPath.getFileSystem(conf);
        FileStatus[] stat = fs.listStatus(inputPath);
		String numDocs = String.valueOf(stat.length);
		conf.set("numDocs", numDocs);

		// Delete output paths if they exist
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(wcOutputPath))
			hdfs.delete(wcOutputPath, true);

		// Create and execute data aggregator job

		Job job = new Job(conf, "DataAggregator");
		job.setJarByClass(DataAggregator.class);
		job.setMapperClass(URLJsonMapper.class);
		job.setReducerClass(URLJsonReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);


		FileInputFormat.setInputPaths(job, wcInputPath);
		FileOutputFormat.setOutputPath(job, wcOutputPath);

        boolean status = job.waitForCompletion(true);
        if (!status) {
            System.exit(1);
        }
    }

	/*
	 * Creates a (key,value) pair for every word in the document
	 *
	 * Input:  ( byte offset , contents of one line )
	 *
	 */
	public static class URLJsonMapper extends Mapper<LongWritable, Text, Text, Text> {

		/************ YOUR CODE HERE ************/

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			String author;
            String book;
            String line = value.toString();
            String[] tuple = line.split("\\n");
            try {
                for(int i=0;i<tuple.length; i++) {
                    JSONObject obj = new JSONObject(tuple[i]);
                    author = obj.getString("tiny");
                    book = obj.getString("long");
                    context.write(new Text(author), new Text(book));
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
	}

    /*
	 * For each identical key, aggregate the values
	 *
	 */
	public static class URLJsonReducer extends Reducer<Text, Text, NullWritable, Text> {

        @Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			try {
				JSONObject obj = new JSONObject();
				JSONArray ja = new JSONArray();
				int sum = 0;
				String longUrl = "";
				for(Text val : values) {
					sum++;
					if (longUrl.equals("")) {
					    longUrl = val.toString();
                    }
				}
				obj.put("count", sum);
				obj.put("tiny", key.toString());
				obj.put("longUrl", longUrl);
				context.write(NullWritable.get(), new Text(obj.toString()));
			} catch(JSONException e) {
				e.printStackTrace();
			}
        }
    }
}
