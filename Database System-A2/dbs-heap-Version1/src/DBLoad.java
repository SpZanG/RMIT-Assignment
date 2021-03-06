import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.dbheap.entity.DataInfo;

public class DBLoad {

	public static void main(String[] args) {
		File file;
		int pageSize, dataSize;
		String path;
		DBLoad load;
		DataInfo info;
		if (args.length != 3 || !args[0].equals("-p") || !(args[1].matches("\\d+"))) {
			System.err.println("Please enter '-p pagesize datafile' as your arguments.");
			return;
		}
		
		file = new File(args[2]);
		if (!file.exists()) {
			System.err.println("Please make sure data file is existed.");
			return;
		}
		load = new DBLoad();
		pageSize = Integer.parseInt(args[1]);
		path = args[2];
		info = load.initDataInfo(path);
		dataSize = pageSize - 4; // 4 is length of counter of data
		
		load.loadAndSaveData(path, dataSize, pageSize, info);
	}
	
	private void loadAndSaveData(String path, int dataSize, int pageSize, DataInfo info) {
		int counter = 0, totalCounter, pageCounter = 0;
		String line;
		DataOutputStream dos = null;
		BufferedReader reader = null;
		ArrayList<String> pageDatas = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		StringBuilder page = new StringBuilder();
		totalCounter = dataSize/info.getRecordFixedSize();
		byte[] pageBytes = new byte[dataSize];
		byte[] pageDataBytes;
		
		try {
			reader = new BufferedReader(new FileReader(path));
			dos = new DataOutputStream(new FileOutputStream("./heap." + pageSize));
			line = reader.readLine();
			// save titles information and data size
			dos.writeInt(info.getRecordFixedSize());
			dos.writeInt(line.getBytes().length);
			dos.writeBytes(line);
			
			while ((line = reader.readLine()) != null) {
				// added space at the end of current record if its length is not long enough
				if (line.length() < info.getRecordFixedSize()) {
					sb.setLength(0);
					for (int i = 0; i < (info.getRecordFixedSize() - line.length()); i++) {
						sb.append(" ");
					}
					line += sb.toString();
				}
				page.append(line);
				counter++;
				
				if (counter == totalCounter) {
					pageCounter++;
					System.out.println("Writing page " + pageCounter + " datas to disk...");
					dos.writeInt(counter);
					counter = 0;
					Arrays.fill(pageBytes, (byte)'0');
					pageDataBytes = page.toString().getBytes();
					
					for (int i = 0; i < pageDataBytes.length; i++) {
						pageBytes[i] = pageDataBytes[i];
					}
					
					dos.writeBytes(new String(pageBytes));
					dos.flush();
					page.setLength(0);
				}
			}
			
			// if datas is not full for one page
			if (counter > 0) {
				pageCounter++;
				System.out.println("Writing page " + pageCounter + " datas to disk...");
				dos.writeInt(counter);
				counter = 0;
				Arrays.fill(pageBytes, (byte)'0');
				pageDataBytes = page.toString().getBytes();
				
				for (int i = 0; i < pageDataBytes.length; i++) {
					pageBytes[i] = pageDataBytes[i];
				}
				
				dos.write(pageDataBytes);
				dos.flush();
				pageDatas.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Heap file creating process is finished.");
	}
	
	private DataInfo initDataInfo(String path) {
		DataInfo data = new DataInfo();
		String line, longestStr = "";
		BufferedReader reader = null;
		int maxLen = 0;
		try {
			reader = new BufferedReader(new FileReader(path));
			line = reader.readLine();
			data.setColumes(line);
			
			// get longest data size
			while ((line = reader.readLine()) != null) {
				if (line.length() > maxLen) {
					maxLen = line.length();
					longestStr = line;
				}
			}
			data.setRecordFixedSize(longestStr.getBytes().length);
			data.setRecordFixedStringLength(longestStr.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

}
