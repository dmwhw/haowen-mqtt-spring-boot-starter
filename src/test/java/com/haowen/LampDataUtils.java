package com.haowen;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 
 * 
 * 
 * //typedef struct<br>
//{<br>
//	unsigned char lable[4]; //AA55AA55    4位  相当于 byte[4]<br>
//	unsigned char cmd[2]; //0100          2位相当于 byte[2]	<br>
//	unsigned short total_pacakge;//该张相片的总包数  2位，相当于byte[2]<br>
//	unsigned short current_pacakge;//当前包，从0开始   2位，相当于 byte[2]<br>
//	unsigned short data_len;//该包实际内容的长度       2位，相当于 byte[2]<br>
//	unsigned int time_stamp;//设备收到用户要拍照的时间戳  4位，相当于byte[4]<br>
//	unsigned char data[16];//包实际内容               char一位相当于 byte,文件直接保存，通过偏移读取<br>
<br>


//}pic_type_t;<br>
//typedef struct<br>
//{<br>
//	unsigned char lable[4]; //AA55AA55<br>
//	unsigned char cmd2; //01	<br>
 *  unsigned char cmd2; //01	<br> //校验位
//	unsigned short total_pacakge;//该张相片的总包数<br>
//	unsigned short current_pacakge;//当前包，从0开始<br>
//	unsigned short data_len;//该包实际内容的长度<br>
//	unsigned int time_stamp;//设备收到用户要拍照的时间戳<br>
//	unsigned char data[16];//包实际内容<br>
//}voice_type_t;<br>
//<br>
 * 
 * 
 * @author haowen
 *
 */
public class LampDataUtils {

	
	public final static Integer CMD_IMAGE=0x01;
	public final static Integer CMD_VOICE=0x02;

	
	public  static class  HeaderFlag{
		private final static int[] FLAG_AA55AA55_val={0xaa,0x55,0xaa,0x55};//
		/**
		 * 用于手机发送到
		 */
		private final static int[] FLAG_55AA55AA_val={0x55,0xaa,0x55,0xaa};
		public final static HeaderFlag FLAG_AA55AA55= new HeaderFlag(FLAG_AA55AA55_val) ;
		public final static HeaderFlag FLAG_55AA55AA= new HeaderFlag(FLAG_55AA55AA_val);
		private int [] value;
		private HeaderFlag(int [] value){
			this.value=value;
		}
		public int[] value(){
			return value; 
		}
	};
	
	
 	 
	/**
	 * 获取包阅读器
	 * @author haowen
	 * @time 2018年8月3日上午11:07:48
	 * @Description  
	 * @param data
	 * @return
	 */
	public static DataFrameReader getFrameReader(byte data[]){
		return new DataFrameReader(data);
	}
	/**
	 * 获取包阅读器
	 * @author haowen
	 * @time 2018年8月3日上午11:07:48
	 * @Description  
	 * @param data
	 * @return
	 */
	public static DataFrameReader getFrameReader(byte data[],boolean inverse){
		return new DataFrameReader(data,inverse);
	}

	/**
	 * 获取MP3分包器,默认反转
	 * @author haowen
	 * @time 2018年8月3日上午11:06:56
	 * @Description  
	 * @param file
	 * @return
	 */
	public static DataDivider getDivider(File file){
		return getDivider(file,true);
	}
	/**
	 * 获取MP3分包器
	 * @author haowen
	 * @time 2018年8月3日上午11:06:56
	 * @Description  
	 * @param file
	 * @return
	 */
	public static DataDivider getDivider(File file,boolean inverse){
		return new DataDivider(file,inverse);
	}
	
	
	
	public static DataDivider getDivider(HeaderFlag flagAa55aa55, int  cmds, File file, boolean b) {
 		return new DataDivider(flagAa55aa55, cmds, file, b);
	}
	
	
	public static DataDivider getDivider(HeaderFlag flagAa55aa55, int  cmds,int perSize, File file, boolean b) {
 		return new DataDivider(flagAa55aa55, cmds,perSize, file, b);
	}
	public static DataDivider getDivider(HeaderFlag flagAa55aa55, int  cmds,int perSize, int timeStamp,File file, boolean b) {
 		return new DataDivider(flagAa55aa55, cmds,perSize,timeStamp, file, b);
	}
	/**
	 * 打包成文件
	 * @author haowen
	 * @time 2018年8月3日上午11:06:42
	 * @Description  
	 * @param file
	 * @param list
	 * @return
	 */
	public static boolean combineDataToFile(File file,List<DataFrameReader> list){
		return FileUtils.writeFileBytes(file, combineDataToBytes(list));
		
	}
	
//	public static boolean combineFileFrameToFile(File file,Collection< byte []> list  ){
//		List<DataFrameReader> drs=new ArrayList<>();
//		for (byte[] frame : list) {
//			drs.add(LampDataUtils.getFrameReader( frame , true));
//		}
//		byte[] bytes = combineDataToBytes(drs);
//		return FileUtils.writeFileBytes(file, bytes);
//		
//	}
	/**
	 * 打包成文件
	 * @author haowen
	 * @time 2018年8月3日上午11:06:42
	 * @Description  
	 * @param file
	 * @param list
	 * @return
	 */
	public static boolean combineFileFrameToFile(File file,File [] list){
		List<DataFrameReader> drs=new ArrayList<>();
		for (File frame : list) {
			drs.add(LampDataUtils.getFrameReader(FileUtils.getFileBytes(frame), true));
		}
		byte[] bytes = combineDataToBytes(drs);
		return FileUtils.writeFileBytes(file, bytes);
		
	}
	/**
	 * 打包成文件
	 * @author haowen
	 * @time 2018年8月3日上午11:06:42
	 * @Description  
	 * @param file
	 * @param list
	 * @return
	 */
	public static boolean combineFileFrameToFileIgnoreLoss(File file,File [] list){
		List<DataFrameReader> drs=new ArrayList<>();
		for (File frame : list) {
			drs.add(LampDataUtils.getFrameReader(FileUtils.getFileBytes(frame), true));
		}
		byte[] bytes = combineDataIgnoreLossToBytes(drs);
		return FileUtils.writeFileBytes(file, bytes);
		
	}
	
	/**
	 * 把帧组装起来
	 * @author haowen
	 * @time 2018年8月3日上午11:04:47
	 * @Description  
	 * @param list
	 * @return
	 */
	public static byte[] combineDataToBytes(List<DataFrameReader> list){
		if (list==null||list.isEmpty()||list.get(0).getTotalPackage()!=list.size()){
			return null;
		}
		Collections.sort(list,new Comparator<DataFrameReader>() {

			@Override
			public int compare(DataFrameReader o1, DataFrameReader o2) {
 				return new Integer(o1.getCurrentPackage()).compareTo(o2.getCurrentPackage());
			}
		});
		ByteArrayOutputStream baos=new ByteArrayOutputStream(list.get(0).getThisDataSize()*list.size());

		for (DataFrameReader dataReader : list) {
			byte[] data = dataReader.getData();
			baos.write(data, 0, dataReader.getThisDataSize());
			System.out.println(dataReader);
		}
		return baos.toByteArray();
	}
	/**
	 * 把帧组装起来
	 * @author haowen
	 * @time 2018年8月3日上午11:04:47
	 * @Description  
	 * @param list
	 * 
	 * @return
	 */
	public static byte[] combineDataIgnoreLossToBytes(List<DataFrameReader> list){
		if (list==null||list.isEmpty()){
			return null;
		}
		
		
		Collections.sort(list,new Comparator<DataFrameReader>() {

			@Override
			public int compare(DataFrameReader o1, DataFrameReader o2) {
 				return new Integer(o1.getCurrentPackage()).compareTo(o2.getCurrentPackage());
			}
		});
		int totalPagePackage=list.get(0).getTotalPackage();
		boolean isHeadLost= list.get(0).getCurrentPackage()!=0; 
		boolean isTailLost= list.get(list.size()-1).getCurrentPackage()!=totalPagePackage-1;	
		int tailLostPackageCount=!isHeadLost?0:(totalPagePackage-list.get(list.size()-1).getCurrentPackage()-1);
		ByteArrayOutputStream baos=new ByteArrayOutputStream(list.get(0).getThisDataSize()*list.size());
		Integer lastPackage=0;
		int dataSize=list.get(0).getThisDataSize();
		byte [] buff=new byte[dataSize];
		//1、补偿目前收到的列表中的空隙
		//补偿头
		if (isHeadLost){
			byte [] header=new byte[dataSize];
			header[0]=new Integer(0xff).byteValue();
			header[1]=new Integer(0xd8).byteValue();
			baos.write(header , 0, dataSize);
			System.err.println("--lost head--");
		}else{
			baos.write(list.get(0).getData() , 0, dataSize);
		}
		// 补偿身体
		for (int i=1;i<list.size();i++){
			DataFrameReader dataFrameReader = list.get(i);
			int delta=dataFrameReader.getCurrentPackage()-lastPackage-1;
			if (delta>0){//有空缺,补充
				System.err.println("--lost--"+(lastPackage+1)+"~"+dataFrameReader.getCurrentPackage());

				for(int j=0;j<delta;j++){
					baos.write(buff , 0, dataSize);
				}
				byte[] data = dataFrameReader.getData();
				baos.write(data, 0, dataFrameReader.getThisDataSize());
			}else if (delta==0){
				byte[] data = dataFrameReader.getData();
				baos.write(data, 0, dataFrameReader.getThisDataSize());
				
			}else {
				//这是因为 重复了才会发生。这个不需要处理
			}
			lastPackage=dataFrameReader.getCurrentPackage();
		}
		
		
		//2、补偿最后没有收到的
		if (tailLostPackageCount>1){

			for(int j=1;j<tailLostPackageCount;j++){
				System.err.println("--lost--tail--"+j);
				baos.write(buff , 0, dataSize);
			}
		}
		//补个尾巴
		if(isTailLost){
			System.err.println("--lost--tail--");

			byte [] tail=new byte[dataSize];
			tail[0]=new Integer(0xff).byteValue();
			tail[1]=new Integer(0xd9).byteValue();
			baos.write(tail , 0, dataSize);
		}
		 byte[] byteArray = baos.toByteArray();
		  try {
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArray;

	}
	/**
	 * 文件分尸器
	 * @author haowen
	 *
	 */
	public final static class DataDivider{
		ByteArrayOutputStream  baos=  new ByteArrayOutputStream() ;
		
		private int label[]={0x55,0xAA,0x55,0xAA} ;//AA55AA55 4bytes
		private int cmd = 0x02 ;//0100 图片 0200音频 2bytes
		private int totalPackage;//总包数  2bytes
		private int timeStamp=0; //4bytes
		//private byte[] data=new byte[0];//
		 
		public boolean inverse=false;
		public int perSize=200;

		File file ;
		
		private DataDivider(File file2, boolean inverse2) {
			this(HeaderFlag.FLAG_55AA55AA,file2,inverse2);
 		}

		
		private DataDivider(HeaderFlag flag,File file2, boolean inverse2) {
			this(flag,  0x02 , 200, file2, inverse2);
		}
		
		
		private DataDivider(HeaderFlag flag,int cmd ,File file2, boolean inverse2) {
			this(flag, cmd, 200, file2, inverse2);
		}
		private DataDivider(HeaderFlag flag,int cmd ,int perSize,File file2, boolean inverse2) {
			this.file=file2;
			this.inverse=inverse2;
			this.label=flag.value;
			this.perSize=perSize;
			this.cmd=cmd;
		}

		public DataDivider(HeaderFlag flag, int  cmd , int perSize2, int timeStamp2, File file2, boolean b) {
			this.file=file2;
			this.inverse=b;
			this.label=flag.value;
			this.perSize=perSize2;
			this.cmd=cmd ;
			this.timeStamp=timeStamp2;
 		}


		private byte[] getheader(int totalPackages){
 			byte [] header=new byte[16];
 			
 			for (int i=0;i< label.length;i++) {
 				header[i]=(byte) label[i];
			}//i=3
 			header[4]=(byte) cmd ;
 			header[5]=00 ;
 			
 			//变化
 			byte[] totalPackageBuff = getUnsignShortByte(totalPackages, inverse);
 			header[6]=totalPackageBuff[0];
 			header[7]=totalPackageBuff[1];

 			
 			//变化
 			//byte[] currentPackageBuff = getUnsignShortByte(currentPackage++, inverse);
 			//header[8]=currentPackageBuff[0];
 			//header[9]=currentPackageBuff[1];
 			
 			//变化
 			//byte[] thisDataSizeBuff = getUnsignShortByte(0, inverse);
 			//header[10]=thisDataSizeBuff[0];
 			///header[11]=thisDataSizeBuff[1];
 			if (timeStamp==0){
 				timeStamp=new Long( System.currentTimeMillis()/1000L).intValue();
 			}
 			byte[] timeStampBuff = getUnsignIntByte(timeStamp, inverse);
 			header[12]=timeStampBuff[0];
 			header[13]=timeStampBuff[1];
 			header[14]=timeStampBuff[2];
 			header[15]=timeStampBuff[3];
 			
 			return header;
		}
		
	

		
		public List<byte[]> getData() throws  Exception{
			
			totalPackage=new Long( file.length()/perSize+(file.length() % perSize==0?0:1)).intValue();
			List<byte[]> list=new ArrayList<>(totalPackage);
			byte[] header = getheader(totalPackage);
			
			
			FileInputStream fis=new FileInputStream(file);
			int len=0;
			byte[] filePiece=new byte[perSize];
			int packageIndex=0;
			while((len=fis.read(filePiece))>0){
				byte []frame=new byte [16+perSize];
				
	 			//当前的包序号
	 			byte[] currentPackageBuff = getUnsignShortByte(packageIndex++, inverse);
	 			header[8]=currentPackageBuff[0];
	 			header[9]=currentPackageBuff[1];
	 			
	 			//这是帧数据大小
	 			byte[] thisDataSizeBuff = getUnsignShortByte(len, inverse);
	 			header[10]=thisDataSizeBuff[0];
	 			header[11]=thisDataSizeBuff[1];
	 			//checksum
	 			//BUGFIX,最后的包，len之后的不能用于计算。必须截断
	 			int dataCheckSum=DataUtils.getUnsignedShortCheckSum(filePiece,0,len);
				//unsigned char的结果
				header[5]= (byte) dataCheckSum ;
	 			//拷贝头
				System.arraycopy(header, 0, frame, 0, 16);
				//拷贝身体
				System.arraycopy(filePiece, 0, frame, 16, len);
				list.add(frame);
			}
			fis.close();
			return list;
		}
		private byte[] getUnsignShortByte(int n,boolean inverse){
			byte [] buff=new byte[2];
			if (!inverse){
				buff[0]=new Integer(( n&0xff00)>>8).byteValue();
				buff[1]=new Integer( (n&0x00ff)>>0).byteValue();
				return buff;
			}
			buff[1]=new Integer( (n&0xff00)>>8).byteValue();
			buff[0]=new Integer( (n&0x00ff)>>0).byteValue();
			return buff;
		}
		private byte[] getUnsignIntByte(int n,boolean inverse){
			byte [] buff=new byte[4];
			if (!inverse){
				buff[0]=new Integer(( n&0xff000000)>>24).byteValue();
				buff[1]=new Integer( (n&0x00ff0000)>>16).byteValue();
				buff[2]=new Integer( (n&0x0000ff00)>>8).byteValue();
				buff[3]=new Integer( (n&0x000000ff)>>0).byteValue();
				return buff;
			}
			buff[3]=new Integer( (n&0xff000000)>>24).byteValue();
			buff[2]=new Integer( (n&0x00ff0000)>>16).byteValue();
			buff[1]=new Integer( (n&0x0000ff00)>>8).byteValue();
			buff[0]=new Integer( (n&0x000000ff)>>0).byteValue();
			return buff;
		}
	}
	
	public final static class DataFrameReader{
		
		private boolean inverse=false;
		
		private byte[] revData ;
		
		private int label[]={0,0,0,0} ;//AA55AA55 4bytes
		private int cmd = 0  ;//0100 图片 0200音频 2bytes
		private int packCheckSum=-1;
		private int totalPackage;//总包数  2bytes
		private int currentPackage;//当前序号 2bytes
		private int thisDataSize;//这个包中数据的大小 2bytes
		private int timeStamp; //4bytes
		private byte[] data=new byte[0];//
		
		private int dataCheckSum=0;
		
		private int type=TYPE_UNKNOWN;
				
		public static Integer TYPE_UNKNOWN=-1;
		public static Integer TYPE_IMAGE=01;
		public static Integer TYPE_VOICE=02;

		private int dataIndex=0;
		private DataFrameReader(byte[] revData){
			this.revData=revData;
			init();
		}
		
		public DataFrameReader(byte[] data2, boolean inverse2) {
			this.inverse=inverse2;
			this.revData=data2;
			init();
 		}



		private void init() {
			if (this.revData==null||this.revData.length<16){
				return ;
			}
			label[0]=revData[dataIndex++];
			label[1]=revData[dataIndex++];
			label[2]=revData[dataIndex++];
			label[3]=revData[dataIndex++];
			
			cmd =revData[dataIndex++];
			packCheckSum=revData[dataIndex++];
			if (cmd ==01){
				type=TYPE_IMAGE;
			}
			if (cmd ==02){
				type=TYPE_VOICE;
			}
			
			totalPackage=getUnsignedShort(getUnsignedByte(revData[dataIndex++]) ,getUnsignedByte(revData[dataIndex++]) ,inverse);
			currentPackage=getUnsignedShort(getUnsignedByte(revData[dataIndex++]),getUnsignedByte(revData[dataIndex++]),inverse);
			thisDataSize=getUnsignedShort(getUnsignedByte(revData[dataIndex++]),getUnsignedByte(revData[dataIndex++]),inverse); 
			timeStamp=getUnsignedInt(getUnsignedByte(revData[dataIndex++]),getUnsignedByte(revData[dataIndex++]),getUnsignedByte(revData[dataIndex++]),getUnsignedByte(revData[dataIndex++])  ,inverse);
			for ( int j=16;revData!=null&&j<revData.length;j++){
				dataCheckSum+= getUnsignedShort(  revData[j],00,inverse ) ;
			}
			dataCheckSum=dataCheckSum&0xFF;
			
			
			
		}

		public byte[] getRevData() {
			return revData;
		}

		public int[] getLabel() {
			return label;
		}

		public int  getCmd() {
			return cmd;
		}

		public int getTotalPackage() {
			return totalPackage;
		}

		public int getCurrentPackage() {
			return currentPackage;
		}

		public int getThisDataSize() {
			return thisDataSize;
		}

		public int getTimeStamp() {
			return timeStamp;
		}

		public byte[] getData() {
			if (thisDataSize>0&&data.length==0){
				data=new byte[thisDataSize];
				try {
					System.arraycopy(revData, 16, data, 0, thisDataSize);;
					return data;
				} catch (Exception e) {
 					e.printStackTrace();
				}
			}
			return data;
		}

		public int getType() {
			return type;
		}

		public int getDataIndex() {
			return dataIndex;
		}

	 
		public int getCheckSum() {
			return dataCheckSum;
		}

		public void setCheckSum(int checkSum) {
			this.dataCheckSum = checkSum;
		}

		@Override
		public String toString() {
			return "DataFrameReader [label=" + Arrays.toString(label) + ", cmd=" +  cmd +" packageCheckSum"+packCheckSum+" hex:"+Integer.toHexString(0xFF& packCheckSum) 
					+ ", totalPackage=" + totalPackage + ", currentPackage=" + currentPackage + ", thisDataSize="
					+ thisDataSize + ", timeStamp=" + timeStamp + ", checkSum=" + dataCheckSum +  "--0x"+Integer.toHexString(0xFF&dataCheckSum)+"isOk?"+checkSumOk()+" "+", type=" + type
					+ ", dataIndex=" + dataIndex + "]";
		}

		public boolean checkSumOk(){
			return  new Integer(0xFF& dataCheckSum ).byteValue()== new Integer(0xFF & packCheckSum ).byteValue();
		}

		private static int getUnsignedByte(byte b){
			int c=b;
 			if (b<0){
				c=b+0xFF+1;
				return c;
			}
			return b;
		}
		 
		private static int getUnsignedShort(int hight,int low,boolean inverses){
			if(inverses){
				return hight<<0|low<<8;
			}
			return hight<<8|low<<0;
		}
		/**
		 * int类型
		 * @author haowen
		 * @time 2018年8月2日下午10:35:55
		 * @Description 
		 * @param h2
		 * @param h1
		 * @param l2
		 * @param l1
		 * @param inverse
		 * @return
		 */
		private static int getUnsignedInt(int h2,int h1,int l2,int l1,boolean inverse){
			if(inverse){
				return h2<<0|h1<<8|l2<<16|l1<<24;
			}
			return  h2<<24|h1<<16|l2<<8|l1<<0;
		}
		
	}
  


	 
}
