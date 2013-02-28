package org.tencompetence.ldauthor.ui.views.inspector;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.tencompetence.ldauthor.LDAuthorPlugin;
import org.tencompetence.ldauthor.ui.common.AbstractToolWindow;
import org.tencompetence.ldauthor.ui.common.StackComposite;


/**
 * Floating Window version of Inspector - not used!
 * 
 * @author Phillip Beauvoir
 * @version $Id: InspectorWindow.java,v 1.6 2009/06/10 10:16:48 phillipus Exp $
 */
public class InspectorWindow extends AbstractToolWindow
implements IContextProvider {
    
    public static String ID = LDAuthorPlugin.PLUGIN_ID + ".inspectorView"; //$NON-NLS-1$
    public static String HELP_ID = LDAuthorPlugin.PLUGIN_ID + ".inspectorViewHelp"; //$NON-NLS-1$
   
    private static final String DIALOG_SETTINGS_SECTION = "InspectorWindowSettings"; //$NON-NLS-1$
    
    private InspectorTitleComposite fTitleBar;
    
    private StackComposite fStackComposite;
    
    private InspectorPageFactory fPageMapper;

    InspectorWindow() {
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.InspectorWindow_0);
    }
    
    @Override
    protected Control createContents(Composite parent) {
        fPageMapper = new InspectorPageFactory();
        
        Composite client = new Composite(parent, SWT.NULL);
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        InspectorWidgetFactory factory = InspectorWidgetFactory.getInstance();
        
        factory.adapt(client);
        factory.paintBordersFor(client);
        
        fTitleBar = new InspectorTitleComposite(client);
        GridData gd = new GridData(GridData.FILL, SWT.NULL, true, false);
        fTitleBar.setLayoutData(gd);
        
        fStackComposite = new StackComposite(client, SWT.NULL);
        gd = new GridData(GridData.FILL, SWT.FILL, true, true);
        fStackComposite.setLayoutData(gd);
        factory.adapt(fStackComposite);
        factory.paintBordersFor(fStackComposite);
        
        setMainTitle(" "); //$NON-NLS-1$
        
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        return client;
    }
    
    public void setMainTitle(String title) {
        fTitleBar.setTitle(title, null);
    }
    
    public void setInput(IInspectorProvider client, Object element) {
        AbstractInspectorPage inspectorPage = fPageMapper.getPage(fStackComposite, element);
        if(inspectorPage != null) {
            inspectorPage.setInput(client, element);
            fStackComposite.setControl(inspectorPage);
        }
    }
    
    @Override
    protected Point getDefaultSize() {
        return new Point(700, 650);
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = LDAuthorPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
        if(section == null) {
            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
        } 
        return section;
    }

    @Override
    public void dispose() {
        super.dispose();
        
        if(fPageMapper != null) {
            fPageMapper.dispose();
        }
    }

    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContextChangeMask()
     */
    public int getContextChangeMask() {
        return NONE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
     */
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    /* (non-Javadoc)
     * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
     */
    public String getSearchExpression(Object target) {
        return Messages.InspectorView_0;
    }

}
