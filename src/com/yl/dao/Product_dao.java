package com.yl.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.yl.dto.Product_dto;

public class Product_dao {
	
	private Product_dao() {
	}
	
	private static Product_dao instance = new Product_dao();
	public static Product_dao getInstance() {
		return instance;
	}
	
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/Oracle11g");
			conn = ds.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	public boolean registProduct(String pcode,String mgid, String pname, int pprice, String pimage,int pstock,String pdescription,int pdiscount) {
		boolean result = false;
		String sql = "INSERT INTO PRODUCT (PCODE,MGID,PNAME,PPRICE,PIMAGE,PSTOCK,PDESCRIPTION,PDISCOUNT) VALUES (?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pcode);
			pstmt.setString(2, mgid);
			pstmt.setString(3, pname);
			pstmt.setInt(4, pprice);
			pstmt.setString(5, pimage);
			pstmt.setInt(6, pstock);
			pstmt.setString(7, pdescription);
			pstmt.setInt(8, pdiscount);
			result = pstmt.executeUpdate()==1;
		} catch (SQLException e) {
			System.out.println("registProduct오류:"+e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	
	private ArrayList<Product_dto> getProductListSQL(int startRow,int endRow,String sql,String searchPname){
		ArrayList<Product_dto> products = new ArrayList<Product_dto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchPname);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pcode = rs.getString("pcode");
				String pname = rs.getString("pname");
				int pprice = rs.getInt("pprice");
				String pimage = rs.getString("pimage");
				int pstock= rs.getInt("pstock");
				String pdescription = rs.getString("pdescription");
				int pdiscount= rs.getInt("pdiscount");
				Date pregist = rs.getDate("pregist");
				int pcumulative_sales= rs.getInt("pcumulative_sales");
				int preview_count= rs.getInt("preview_count");
				double prating = rs.getDouble("prating");
				String mgname= rs.getString("mgname");
				products.add(new Product_dto(pcode, pname, pprice, pimage, pstock, pdescription, pdiscount, pregist, pcumulative_sales, prating, preview_count, mgname));
			}
		} catch (SQLException e) {
			System.out.println("getProductListDesc오류 :"+e.getMessage());
		} finally {
				try {
					if(rs!=null) rs.close();
					if(pstmt!=null) pstmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			
		}
		
		return products;
	}
	
	public ArrayList<Product_dto> getProductListSort(int startRow,int endRow,String sortingCriteria,String searchPname){
		String sql = "";
		if(sortingCriteria.equals("lowprice")) {
			sql = "SELECT * FROM (SELECT ROWNUM RN, A.* FROM (SELECT P.*,M.MGNAME FROM PRODUCT P,MANAGER M WHERE PNAME LIKE '%'||?||'%' AND P.MGID = M.MGID ORDER BY pprice, preview_count DESC) A)"  +
					" WHERE RN BETWEEN ? AND ?";
		}else if(sortingCriteria.equals("pregist")) {
			sql = "SELECT * FROM (SELECT ROWNUM RN, A.* FROM (SELECT P.*,M.MGNAME FROM PRODUCT P,MANAGER M WHERE PNAME LIKE '%'||?||'%' AND P.MGID = M.MGID ORDER BY pregist DESC, preview_count DESC) A)"  +
					" WHERE RN BETWEEN ? AND ?";
		}else if(sortingCriteria.equals("preview_count")) {
			sql = "SELECT * FROM (SELECT ROWNUM RN, A.* FROM (SELECT P.*,M.MGNAME FROM PRODUCT P,MANAGER M WHERE PNAME LIKE '%'||?||'%' AND P.MGID = M.MGID ORDER BY preview_count DESC) A)"  +
					" WHERE RN BETWEEN ? AND ?";
		}else if(sortingCriteria.equals("highprice")) {
			sql = "SELECT * FROM (SELECT ROWNUM RN, A.* FROM (SELECT P.*,M.MGNAME FROM PRODUCT P,MANAGER M WHERE PNAME LIKE '%'||?||'%' AND P.MGID = M.MGID ORDER BY pprice DESC, preview_count DESC) A)"  +
					" WHERE RN BETWEEN ? AND ?";
		}
		return getProductListSQL(startRow, endRow, sql,searchPname);
	}
	
	public Product_dto[] getProductTOPN(int n){
		String sql = "SELECT P.*,MG.MGNAME FROM PRODUCT P,MANAGER MG WHERE P.MGID=MG.MGID ORDER BY P.PCUMULATIVE_SALES DESC";
		Product_dto[] products = new Product_dto[n];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i=0;i<n;i++) {
				rs.next();
				String pcode = rs.getString("pcode");
				String pname = rs.getString("pname");
				int pprice = rs.getInt("pprice");
				String pimage = rs.getString("pimage");
				int pstock= rs.getInt("pstock");
				String pdescription = rs.getString("pdescription");
				int pdiscount= rs.getInt("pdiscount");
				Date pregist = rs.getDate("pregist");
				int pcumulative_sales= rs.getInt("pcumulative_sales");
				int preview_count= rs.getInt("preview_count");
				double prating = rs.getDouble("prating");
				String mgname= rs.getString("mgname");
				products[i] = new Product_dto(pcode, pname, pprice, pimage, pstock, pdescription, pdiscount, pregist, pcumulative_sales, prating, preview_count, mgname);
			}
		} catch (SQLException e) {
			System.out.println("getProductTOPN오류 :"+e.getMessage());
		} finally {
				try {
					if(rs!=null) rs.close();
					if(pstmt!=null) pstmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			
		}
		return products;
	}
	
	
	
	public int getTotalNumberProductSearch(String searchPname) {
		int result = 0;
		String sql = "SELECT COUNT(*) FROM PRODUCT WHERE PNAME LIKE '%'||?||'%'";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchPname);
			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
				try {
					if(rs!=null) rs.close();
					if(pstmt!=null) pstmt.close();
					if(conn!=null) conn.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			
		}
		return result;
	}
	
	//상품 상세정보
	public Product_dto productDetail(String pcode) {
		Product_dto product = null;
		String sql = "SELECT P.*,MGNAME FROM PRODUCT P,MANAGER M WHERE P.MGID=M.MGID AND PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pcode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String pname = rs.getString("pname");
				int pprice = rs.getInt("pprice");
				String pimage = rs.getString("pimage");
				int pstock = rs.getInt("pstock");
				String pdescription = rs.getString("pdescription");
				int pdiscount = rs.getInt("pdiscount");
				Date pregist = rs.getDate("pregist");
				int pcumulative_sales = rs.getInt("pcumulative_sales");
				int preview_count = rs.getInt("preview_count");
				double prating = rs.getDouble("prating");
				String mgname = rs.getString("mgname");
				product = new Product_dto(pcode, pname, pprice, pimage, pstock, pdescription, pdiscount, pregist, pcumulative_sales, prating, preview_count, mgname);
			}
		} catch (SQLException e) {
			System.out.println("dd"+e.getMessage());
		} finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return product;
	}
	
	public boolean modifyProduct(String pname,int pprice, String pimage,int pstock,String pdescription,int pdiscount, String pcode) {
		boolean result = false;
		//정보 수정시 날짜는 최신, 누적판매량은 0으로 초기화
		String sql = "UPDATE PRODUCT SET PNAME=?, PPRICE=?, PIMAGE=?, PSTOCK=?, "
				+ "PDESCRIPTION=?,PDISCOUNT=?, PREGIST=SYSDATE,PCUMULATIVE_SALES=0 WHERE PCODE=?";
		// 1.PNAME 2.PPRICE 3.PIMAGE 4.PSTOCK 5.PDESCRIPTION 6.PDISCOUNT 7.PCODE
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pname);
			pstmt.setInt(2, pprice);
			pstmt.setString(3, pimage);
			pstmt.setInt(4, pstock);
			pstmt.setString(5, pdescription);
			pstmt.setInt(6, pdiscount);
			pstmt.setString(7, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean productStockMinus(String pcode,int pcnt) {
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PSTOCK=PSTOCK-? WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pcnt);
			pstmt.setString(2, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean productStockPlus(String pcode,int pcnt) {
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PSTOCK=PSTOCK+? WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pcnt);
			pstmt.setString(2, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean deleteProduct(String pcode) {
		boolean result = false;
		String sql = "DELETE FROM PRODUCT WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean pcumulative_sales_plus(String pcode,int pcnt) {
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PCUMULATIVE_SALES=PCUMULATIVE_SALES+? WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pcnt);
			pstmt.setString(2, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean modifyProductDiscount(int pdiscount,String mgid) {
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PDISCOUNT = ? WHERE MGID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pdiscount);
			pstmt.setString(2, mgid);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	public double getPRating(String pcode) {
		double result = 0.0;
		String sql = "SELECT NVL(ROUND(AVG(RSTAR),1),-1) FROM REVIEW R,PRODUCT P WHERE R.PCODE=P.PCODE AND P.PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pcode);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getDouble(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
	public boolean modifyPRating(String pcode) {
		double prating = getPRating(pcode);
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PRATING=? WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, prating);
			pstmt.setString(2, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	public boolean PReview_count_plus(String pcode) {
		boolean result = false;
		String sql = "UPDATE PRODUCT SET PREVIEW_COUNT=PREVIEW_COUNT+1 WHERE PCODE=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pcode);
			result = pstmt.executeUpdate()==1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
	
}
