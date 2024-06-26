package Connection;

import main.loginGUI;
import sendfile.client.LoginForm;
import Connection.DatabaseConnection;
import com.raven.event.PublicEvent;
import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Connection;
import java.util.Vector;
import sendfile.client.chatGUI;
//import sun.security.provider.MD5;
public class AddUser{
    
    private static String  UserName;
    
    private static Vector<String> vData=null;

    public String getUsername(){
        return this.UserName;
    }
    
    
    
    public  static void addUserAccount(String username, String password) throws NoSuchAlgorithmException{
        MD5 lib = new MD5();
        String encryptedPassword = lib.md5(password);
        
        
        
        DatabaseConnection.connectToDatabase();
        Connection conn = DatabaseConnection.getConnection();
        System.out.println(conn);
        UserName = username;
        try{
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery("SELECT UserName FROM UserAccount ");
            ResultSetMetaData rstmeta = rst.getMetaData();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO UserAccount(UserName,Password)\n" +
                                                                                                "VALUES(?,?);");


            int num_column = rstmeta.getColumnCount();
            /*vData chứa nội dung của bảng */
            vData = new Vector(10,10);
            while (rst.next())
            {
                vData.add(rst.getString(1));
            }

            int t=1;
            for (String id: vData){
                if (id.equalsIgnoreCase(username)){
                    t=0;
                    break;
                }
            }
            if (t==1){
                ps.setString(1,username);
                ps.setString(2,encryptedPassword);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null,"Tạo tài khoản thành công","Notification",JOptionPane.INFORMATION_MESSAGE);
//                PublicEvent.getInstance().getEventMain().initChat();
//          mở cửa sổ chat tại đây
                loginGUI.connectToServer(username);
                   
                
            } else{
                JOptionPane.showMessageDialog(null,"Tài khoản này đã tồn tại","Warning",JOptionPane.INFORMATION_MESSAGE);
            }


        }catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }


    }
    
    
    
    
    public static void login(String user, String pass){
        MD5 lib = new MD5();
        String encryptedPass = lib.md5(pass);
        int t=0;
        String userRs="",passRs="",msg="",typeUser="";;
        Vector<String> v = new Vector<String>();

        DatabaseConnection.connectToDatabase();
        Connection conn = DatabaseConnection.getConnection();

        try
        {

            Statement sm = conn.createStatement();
            /*Truy vấn thông tin từ bảng students */
            ResultSet rs=sm.executeQuery("Select UserName, Password from UserAccount");
            /*Tạo ResultSetMetaData để lấy thông tin của ResultSet*/
            ResultSetMetaData rsm= rs.getMetaData();
            /*Lấy thông tin số cột của bảng students từ đối tượng rsm*/
            int col_num = rsm.getColumnCount();
            while (rs.next()){
                if (user.equals(rs.getString(1).strip())){
                    userRs=rs.getString(1).strip();
                    passRs=rs.getString(2).strip();
                    if (encryptedPass.equals(rs.getString(2))){
                     
                        msg="Login successfully !";
                        loginGUI.connectToServer(user);
                    } 
                    else{
//                            sayWrongPassword(); error
                    }
                }
            }
        } catch( Exception e )
        {
            System.out.print("BUG: ");
            System.out.println( e ) ;
        }
//        v.add(msg);
//        v.add(typeUser);

    }
    
    public void sayWrongPassword(){
        JOptionPane.showMessageDialog(null, "Sai mật khẩu", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
//    Encrypt password tên hàm 
    private static String  encryptPassword(String password) throws NoSuchAlgorithmException{
        
        MessageDigest md;
        String result = "";
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            BigInteger bi = new BigInteger(1, md.digest());
			
            result = bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
	e.printStackTrace();
        }
        return result;
    }
    
    
}


class MD5 {
//	this class uses for encrypt user password to save in database
	public String md5(String str){
		MessageDigest md;
		String result = "";
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			BigInteger bi = new BigInteger(1, md.digest());
			
			result = bi.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
        }
}