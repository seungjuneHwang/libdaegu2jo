package kr.co.daegu.member;

import java.io.Serializable;

public class MemberDTO implements Serializable {
	
	private String id;// ���̵�
	private String pw;// ��й�ȣ
	private String pwch;// ��й�ȣȮ��
	private String irum;// �̸�
	private String tel;// ��ȭ��ȣ
	private String email;// �̸���
	private String birth;// �������
	private String memImage;// ������
	private String emailcheck;// �̸��ϼ��ſ���
	private String smscheck;// ���ڼ��ſ���
	private int visible;// ȸ�����翩�� Default=1

	public MemberDTO() {
		super();
	}

	public MemberDTO(String id, String pw, String pwch, String irum, String tel, String email, String birth,
			String memImage, String emailcheck, String smscheck, int visible) {
		super();
		this.id = id;
		this.pw = pw;
		this.pwch = pwch;
		this.irum = irum;
		this.tel = tel;
		this.email = email;
		this.birth = birth;
		this.memImage = memImage;
		this.emailcheck = emailcheck;
		this.smscheck = smscheck;
		this.visible = visible;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getPwch() {
		return pwch;
	}

	public void setPwch(String pwch) {
		this.pwch = pwch;
	}

	public String getIrum() {
		return irum;
	}

	public void setIrum(String irum) {
		this.irum = irum;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getMemImage() {
		return memImage;
	}

	public void setMemImage(String memImage) {
		this.memImage = memImage;
	}

	public String getEmailcheck() {
		return emailcheck;
	}

	public void setEmailcheck(String emailcheck) {
		this.emailcheck = emailcheck;
	}

	public String getSmscheck() {
		return smscheck;
	}

	public void setSmscheck(String smscheck) {
		this.smscheck = smscheck;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", pw=" + pw + ", pwch=" + pwch + ", irum=" + irum + ", tel=" + tel + ", email="
				+ email + ", birth=" + birth + ", memImage=" + memImage + ", emailcheck=" + emailcheck + ", smscheck="
				+ smscheck + ", visible=" + visible + "]";
	}

}
