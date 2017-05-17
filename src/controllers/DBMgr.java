package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.MemberBean;
import models.SeatBean;
import models.Selected;
import models.TheaterBean;

public class DBMgr {
	DBConnection db;

	final int EVENT_POINT = 5000; // 회원가입 이벤트 point

	public DBMgr() {
		db = new DBConnection();
	}

	// ~영화를 상영하는 ~극장의 ~일자의 상영시간 리스트 불러오기
	public ArrayList<String> getTime(String title, String theater, String date) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select time from movie_tb m left join schedule_tb s on m.no = s.movie_no right join theater_tb t on s.theater_no = t.no where m.title=?and t.name=? and s.date=? ";

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, theater);
			pstmt.setString(3, date);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// if (conn != null) {
			// try {
			// conn.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
		return list;
	}

	// ~영화를 상영하고 있는 극장 리스트 불러오기
	public ArrayList<TheaterBean> getTheaters(String title) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<TheaterBean> list = new ArrayList<>();
		String sql = "select distinct t.name from movie_tb m left join schedule_tb s on m.no = s.movie_no right join theater_tb t on s.theater_no = t.no  where m.title = ?";
		TheaterBean bean;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TheaterBean();
				bean.setName(rs.getString("name"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// if (conn != null) {
			// try {
			// conn.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
		return list;
	}

	// 좌석정보 불러오기
	public ArrayList<SeatBean> getSeats(String theater, String date) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<SeatBean> list = new ArrayList<>();
		String sql = "select seat_no, date, state from theater_tb join seat_tb where theater_tb.name=? and date=?";
		SeatBean bean;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, theater);
			pstmt.setString(2, date);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SeatBean();
				bean.setSeatNo(rs.getString("seat_no"));
				bean.setState(rs.getString("state"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// if (conn != null) {
			// try {
			// conn.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
		return list;
	}

	// 달력에서 날짜를 누르면 그 날짜에 상영하는 회차 목록(극장이름, 시간)이 return되는 메서드
	@Deprecated // TODO : 제거
	public ArrayList<String> getSchedule(String title, String date) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select t.name, s.time from movie_tb m left join schedule_tb s on m.no = s.movie_no right join theater_tb t on s.theater_no = t.no where m.title = ? and s.date = ?";
		Selected selected;

		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, date);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("name") + ", " + rs.getString("time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// if (conn != null) {
			// try {
			// conn.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
		return list;
	}

	public ArrayList<MemberBean> signUp(String id, String password, String name, String birthdate, String phone) {
		Connection conn = null;
		Statement stmt = null;
		ArrayList<MemberBean> list = new ArrayList<MemberBean>();
		String sql = "INSERT INTO MEMBER_TB VALUES (null, '" + id + "','" + password + "','" + name + "','" + birthdate
				+ "','" + phone + "','" + EVENT_POINT + "')";

		try {
			conn = db.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public ArrayList<MemberBean> login() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<MemberBean> list = new ArrayList<MemberBean>();
		String sql = "SELECT * FROM MEMBER_TB";
		MemberBean bean;

		try {
			conn = db.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bean = new MemberBean();
				bean.setId(rs.getString("id"));
				bean.setPassword(rs.getString("password"));
				bean.setName(rs.getString("name"));
				bean.setBirthdate(rs.getString("birthdate"));
				bean.setPhone(rs.getString("phone"));
				bean.setPoint(rs.getInt("point"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// if (conn != null) {
			// try {
			// conn.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
		return list;
	}
}
