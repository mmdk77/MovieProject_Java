package com.sds.movie.client;

import java.io.File;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

public class Barcord {

	public File createDir(String filePath){
		File SaveDir = new File(filePath);
		if (!SaveDir.exists()) {
		    System.out.println("creating directory: "+filePath);
		    boolean result = false;

		    try{
		    	SaveDir.mkdirs();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("�������� ����");
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		return SaveDir;
	}
	
	public Barcord(String cord,String filePath,String fileName) {
		String str = cord;
		try {
			Barcode barcode = BarcodeFactory.createCode128B(str);
			barcode.setBarHeight(200); //��������
			//barcode.setDrawingText(false); //�ؽ�Ʈ �Ⱥ��̰� �ϱ�!
			//barcode.setLabel("Barcode creation test...");//���ڵ� �� ����!!
			createDir(filePath);
			File file = new File(filePath+fileName);
			BarcodeImageHandler.saveJPEG(barcode, file);
		} catch (BarcodeException e) {
			e.printStackTrace();
		} catch (OutputException e) {
			e.printStackTrace();
		}
		
	}

}
