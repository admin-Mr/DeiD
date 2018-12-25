package util.openwin;

public class QryField {
	private String FieldName;
	private String ShowText;
	private String Type;
	private String Width;
	private int Index;
	
	public QryField(String FieldName, String ShowText, String Type, int Index,String Width) {
		this.FieldName = FieldName;
		this.ShowText = ShowText;
		this.Type = Type;
		this.Index = Index;
		this.Width = Width;
	}

	public String getWidth() {
		return Width;
	}

	public void setWidth(String width) {
		Width = width;
	}

	public String getFieldName() {
		return FieldName;
	}
	
	public String getShowText() {
		return ShowText;
	}
	
	public String getType() {
		return Type;
	}
	
	public int getIndex() {
		return Index;
	}
	
	public String toString() {
		return ShowText;
	}
}

