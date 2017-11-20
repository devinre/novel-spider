package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.jsoup.nodes.Document;

import getinformation.Getinformation;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("输入小说目录网址：");
		Scanner scan=new Scanner(System.in);
		String read=scan.nextLine();
		scan.close();
		Document novel=Getinformation.getdocument(read);
		String novelname=Getinformation.gettitle(novel)+".txt";
		String[] novelcatalog=Getinformation.geturl(read);
		for(int i=0;i<novelcatalog.length;i++) {
			System.out.println("正在处理第"+(i+1)+"/"+(novelcatalog.length)+"部分");
			FileOutputStream fs;
			try {
				fs = new FileOutputStream(new File(novelname),true);
				PrintStream p = new PrintStream(fs);
	            p.println(Getinformation.gettitle(Getinformation.getdocument(novelcatalog[i])));
	            p.println(Getinformation.getcontent(Getinformation.getdocument(novelcatalog[i]))); 
	            p.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

}
