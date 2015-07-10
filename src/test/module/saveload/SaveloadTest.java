package test.module.saveload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class SaveloadTest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Test
	public void writeObjTest(){
		SerializeObj so = new SerializeObj();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("foo.txt"));
			oos.writeObject(so);
			
			so = new SerializeObj();
			so.x = 4545;
			so.z = "blabla";
			oos.writeObject(so);
			
			T1 ttt = new T1("lualala");
			oos.writeObject(ttt);
			oos.close();
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("foo.txt"));
			SerializeObj sooo = (SerializeObj) ois.readObject();
			System.out.println(String.format("x = %d, y = %f, z = %s\n", sooo.x, sooo.y, sooo.z));
			
			sooo = (SerializeObj) ois.readObject();
			System.out.println("x = " + sooo.x);
			System.out.println("t1 name = " + sooo.tttt.name);
			
			T1 toto = (T1) ois.readObject();
			System.out.println("toto's name = " + toto.name);
			
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class SerializeObj implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4703871686818333382L;
		
		public int x;
		public double y;
		public String z;
		public T1 tttt;
		
		public SerializeObj(){
			this.x = 3;
			this.y = 4.0;
			this.z = "testest";
			tttt = new T1("olaola");
		}
	}
	
	private class T1 implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7677432510945846505L;
		public String name;
		
		public T1(String name){
			this.name = name;
			System.out.println("new, name = " + name);
		}
	}
}
