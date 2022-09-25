package cnuphys.cnf.event.namespace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.jlab.io.base.DataEvent;
import org.jlab.jnp.hipo4.data.Schema;

import cnuphys.cnf.event.dictionary.Column;

/**
 * A set of static convenience methods leveraging the NameSpace
 * 
 * @author heddle
 *
 */
public class DataUtils {

	// the name space manager
	private static NameSpaceManager _nameSpace = NameSpaceManager.getInstance();

	/**
	 * Get the int data type for a column
	 * 
	 * @param bankName   the name of the bank
	 * @param columnName the name of the column
	 * @return the integer data type or -1 on error
	 */
	public static int getDataType(String bankName, String columnName) {

		BankInfo bankInfo = _nameSpace.getBank(bankName);
		if (bankInfo != null) {
			ColumnInfo columnInfo = bankInfo.getColumn(columnName);
			if (columnInfo != null) {
				return columnInfo.getType();
			}
		}
		return -1;

	}

	/**
	 * Get a list of all full column info objects for any banks that have data in the given
	 * event. This is used by the Node
	 *
	 * @param event the event in question
	 * @return a list of all full column names with data in the given event
	 */

	public static ArrayList<ColumnInfo> columnsWithData(DataEvent event) {

		ArrayList<ColumnInfo> fullColumnList = new ArrayList<>();

		int colorIndex = 0;
		
		if (event != null) {

			String bankList[] = event.getBankList();
			if (bankList != null) {
				Arrays.sort(bankList);
				for (String bankName : bankList) {
					colorIndex++;
					BankInfo bankInfo = _nameSpace.getBank(bankName);
					
					if (bankInfo != null) {
						for (ColumnInfo columnInfo : bankInfo) {
							columnInfo.colorIndex = colorIndex;
							fullColumnList.add(columnInfo);
						}
						
						
					}
					
				}
			}
		} //event not null

		return fullColumnList;

	}
}
