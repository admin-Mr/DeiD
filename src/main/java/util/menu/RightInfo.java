/**
 * Project:		Web���Ψt�ζ}�o���x
 * Filename: 	RightInfo.java
 * Author: 		wilson
 * Create Date:	2005/11/28
 * Version: 	0.90
 * Description:	�{�������v����T
 */

package util.menu;

import java.io.Serializable;

/**
 * �{�������v����T RightInfo
 */
public class RightInfo implements Serializable {
	private boolean execute = true;			//執行/查詢 權限
	private boolean add = true;				//新增
	private	boolean edit = true;			//修改
	private boolean delete = true;			//刪除
	private boolean confirm = true;			//確認
	private boolean cancelConfirm = true;	//取消確認
	private boolean cost = true;			//成本
	private boolean print = true;			//列印
	private boolean other[];				//其他權限
	
	public RightInfo() {
	}		
	
	public RightInfo(String right[]) {
		if (right.length >=1) execute = right[0].equalsIgnoreCase("Y");
		if (right.length >=2) add = right[1].equalsIgnoreCase("Y");
		if (right.length >=3) edit = right[2].equalsIgnoreCase("Y");
		if (right.length >=4) delete = right[3].equalsIgnoreCase("Y");
		if (right.length >=5) confirm = right[4].equalsIgnoreCase("Y");
		if (right.length >=6) cancelConfirm = right[5].equalsIgnoreCase("Y");
		if (right.length >=7) cost = right[6].equalsIgnoreCase("Y");
		if (right.length >=8) print = right[7].equalsIgnoreCase("Y");
		if (right.length > 8) {
			other = new boolean[right.length-8];
			for(int i=8; i<right.length; i++) other[i-8] = right[i].equalsIgnoreCase("Y");
		}
		else	other = new boolean[0];
	}		
	/**
	 * 執行權限
	 *
	 * @return Return the exectue.
	 */
	public boolean isExecute() {
		return execute;
	}

	/**
	 * 新增權限
	 *
	 * @return Return the add.
	 */
	public boolean isAdd() {
		return add;
	}
	
	/**
	 * 修改權限
	 *
	 * @return Return the edit.
	 */
	public boolean isEdit() {
		return edit;
	}
	
	/**
	 * 刪除權限
	 *
	 * @return Return the delete.
	 */
	public boolean isDelete() {
		return delete;
	}
	
	/**
	 * 確認權限
	 *
	 * @return Return the confirm.
	 */
	public boolean isConfirm() {
		return confirm;
	}
	
	/**
	 * 取消確認權限
	 *
	 * @return Return the cancelConfirm.
	 */
	public boolean isCancelConfirm() {
		return cancelConfirm;
	}
	
	/**
	 * 成本權限
	 *
	 * @return Return the cost.
	 */
	public boolean isCost() {
		return cost;
	}
	
	/**
	 * 說明：列印

	 *
	 * @return Return the print.
	 */
	public boolean isPrint() {
		return print;
	}
	
	public boolean getUserDefine(int index) {
		return other[index];
	}

}
