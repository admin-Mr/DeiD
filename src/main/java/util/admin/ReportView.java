package util.admin;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class ReportView extends SelectorComposer<Component> {	
	@Wire
	private Window winPreview;
	@Wire
	private Iframe iframe;
	
	public void doAfterCompose(Component window) throws Exception{
		super.doAfterCompose(window);
		
		Execution exe = Executions.getCurrent();
		AMedia amedia = (AMedia) exe.getArg().get("amedia");
		iframe.setContent(amedia);
	}	
}
