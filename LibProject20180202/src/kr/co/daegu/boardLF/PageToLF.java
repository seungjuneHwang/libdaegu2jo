package kr.co.daegu.boardLF;

import java.io.Serializable;
import java.util.ArrayList;

public class PageToLF implements Serializable {

	private ArrayList<BoardLfDTO> list; // ��ϸ���Ʈ����
	private int curPage;// ���� ������ ��ȣ
	private int perPage;// �������� ������ ���ڵ� ����
	private int totalCount;// ��ü���ڵ尳��

	public PageToLF() {

		this.perPage = 5;
	}

	public ArrayList<BoardLfDTO> getList() {
		return list;
	}

	public void setList(ArrayList<BoardLfDTO> list) {
		this.list = list;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "PageTo [list=" + list + ", curPage=" + curPage + ", perPage=" + perPage + ", totalCount=" + totalCount
				+ "]";
	}
}
