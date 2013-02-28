package org.tencompetence.qtieditor.ui;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tencompetence.ldauthor.ui.dialogs.Messages;


/**
 * New file dialog - copied from ld author and modified
 * 
 * @author Phillip Beauvoir
 * modified by Yongwu Miao
 */
public class NewTestDialog
extends TitleAreaDialog {
	
    /*
     * Text Controls
     */
    private Text fText;

    /**
     * The file
     */
    private File fFile;
    
    private File fParentFolder;

    /**
	 * Constructor
     * @param parentShell
	 * @param parent Parent to add to
	 */
	public NewTestDialog(File parentFolder, Shell parentShell) {
        super(parentShell);
        setTitleImage(ImageFactory.getImage(ImageFactory.ICON_NEW_TEST_FOLDER));
        fParentFolder = parentFolder;
	}
	
    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("New Test");
        shell.setImage(ImageFactory.getImage(ImageFactory.ICON_NEW_TEST));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("New Test");
        setMessage("Create a new test. Please do not use any white-space in the name like 'A test'.");
        
        Composite composite = new Composite((Composite)super.createDialogArea(parent), SWT.NULL);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 10;
        layout.marginTop = 5;
        composite.setLayout(layout);
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);
        
        // Name
        Label label = new Label(composite, SWT.NULL);
        label.setText(Messages.NewFileDialog_2);
        
        fText = new Text(composite, SWT.BORDER);
        fText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        
        return composite;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /**
     * @return The chosen folder or null
     */
    public File getFile() {
        return fFile;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        fFile = new File(fParentFolder, fText.getText());
        super.okPressed();
    }

    /**
     * Validate this input
     */
    private void validateInput() {
        String errorMessage = isValid(fText.getText());
        setErrorMessage(errorMessage);
        getButton(IDialogConstants.OK_ID).setEnabled(errorMessage == null);
    }

    /**
     * Validate user input
     */
    private String isValid(String newText) {
        if("".equals(newText.trim())) { //$NON-NLS-1$
            return Messages.NewFileDialog_3;
        }
        
        if(newText.indexOf(" ")!=-1) {
        	return "Please do not input any white-space in the name!";
        }
    
        File file = new File(fParentFolder, newText);
        if(file.exists()) {
            return Messages.NewFileDialog_4;
        }

        file = new File(fParentFolder, newText+".xml");
        if(file.exists()) {
            return Messages.NewFileDialog_4;
        }

        // This will ensure non-legal filename characters are disallowed
        IStatus result = ResourcesPlugin.getWorkspace().validateName(newText, IResource.FILE);
        if(!result.isOK())  {
            return Messages.NewFileDialog_5;
        }
        
        return null;
    }
}
