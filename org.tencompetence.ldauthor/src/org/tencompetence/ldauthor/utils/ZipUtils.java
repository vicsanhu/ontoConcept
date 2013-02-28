/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.ldauthor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Some useful Zip Utilities
 *
 * @author Phillip Beauvoir
 * @author Paul Sharples
 * @version $Id: ZipUtils.java,v 1.4 2009/10/16 13:54:07 phillipus Exp $
 */
public final class ZipUtils {
	
    /**
     * Add all files and sub-files to the Zip
     * @param srcFolder The folder to add to the zip file
     * @param zOut
     * @param exclude A list of files to exclude from the operation. Can be null.
     * @param progressMonitor an optional ProgressMonitor.  This can be null.
     * @throws IOException If error or user cancelled
     */
    public static void addFolderToZip(File srcFolder,
                                      ZipOutputStream zOut,
                                      File[] exclude,
                                      IProgressMonitor progressMonitor) throws IOException {
        if(!srcFolder.isDirectory()) {
            throw new IOException("Not a folder"); //$NON-NLS-1$
        }
        
        addFolderToZip(srcFolder, srcFolder, zOut, exclude, progressMonitor);
    }
    
    private static void addFolderToZip(File rootFolder,
                                       File srcFolder,
                                       ZipOutputStream zOut,
                                       File[] exclude,
                                       IProgressMonitor progressMonitor) throws IOException {
        
        File[] files = srcFolder.listFiles();
        
        for(int i = 0; i < files.length; i++) {
            // If we have a Progress Monitor...
            if(progressMonitor != null) {
                progressMonitor.setTaskName(files[i].getName());
                if(progressMonitor.isCanceled()) {
                    throw new IOException("User cancelled."); //$NON-NLS-1$
                }
            }
            
            // Sub-folder
            if(files[i].isDirectory()) {
                addFolderToZip(rootFolder, files[i], zOut, exclude, progressMonitor);
            }
            
            else {
                boolean do_add_file = true;
                
                // Check for excluded file
                if(exclude != null) {
                    for(File file_exclude : exclude) {
                        if(file_exclude.equals(files[i])) {
                            do_add_file = false;
                            break;
                        }
                    }
                }
                    
                if(do_add_file) {
                    // Get a relative path
                    String entryName = FileUtils.getRelativePath(files[i], rootFolder);
                    addFileToZip(files[i], entryName, zOut);
                }
            }
        }        
    }
    
	/**
	 * Adds a file to the Zip file
	 * @param file The file to add
	 * @param entryName
	 * @param zOut
	 * @throws IOException
	 */
	public static void addFileToZip(File file, String entryName, ZipOutputStream zOut) throws IOException {
        // Don't add directories
        if(file.isDirectory()) {
            return;
        } 
        
        int bytesRead;
        final int bufSize = 1024;
        byte buf[] = new byte[bufSize];
        
        ZipEntry zipEntry = new ZipEntry(entryName);
        // Set time stamp to file's
        zipEntry.setTime(file.lastModified());
        
        try {
            zOut.putNextEntry(zipEntry);
        }
        catch(IOException ex) {
            /*
             * Ignore things like duplicate entries and just carry on
             */
            return;
        }
        
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), bufSize);
        while((bytesRead = in.read(buf)) != -1) {
            zOut.write(buf, 0, bytesRead);
        }
        zOut.closeEntry();
        in.close();
    }

    /**
	 * Adds a String as a field entry to an already opened ZipOutputStream
	 * @param text
	 * @param entryName
	 * @param zOut
	 * @throws IOException
	 */
	public static void addStringToZip(String text, String entryName, ZipOutputStream zOut) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(text));
		
		ZipEntry zipEntry = new ZipEntry(entryName);
		
		zOut.putNextEntry(zipEntry);
		
		int i;
		while((i = reader.read()) != -1) {
			zOut.write(i);
		}
		zOut.closeEntry();
	}
	
	/**
	 * @param zipFile
	 * @param entryName
	 * @return True if the zip file has an entry
	 * @throws IOException
	 */
	public static boolean hasZipEntry(File zipFile, String entryName) throws IOException {
		ZipEntry zipEntry;
		ZipInputStream zIn;
		boolean foundEntry = false;
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
		zIn = new ZipInputStream(in);
		
		// Get zip entry
		while((zipEntry = zIn.getNextEntry()) != null) {
			String zipEntryName = zipEntry.getName();
			if(zipEntryName.equalsIgnoreCase(entryName)) {
				foundEntry = true;
				break;
			}
			zIn.closeEntry();
		}
		
		zIn.close();
		
		return foundEntry;
	}

	/**
	 * Extracts a named entry out of the zip file and returns the entry as a String
	 * Returns null if weirdness happens
	 * @param zipFile
	 * @param entryName
	 * @return
	 * @throws IOException
	 */
	public static String extractZipEntry(File zipFile, String entryName) throws IOException {
		ZipEntry zipEntry;
		ZipInputStream zIn;
		int bit;
		boolean foundEntry = false;
		StringBuffer sb;
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
		zIn = new ZipInputStream(in);
		
		// Get zip entry
		while((zipEntry = zIn.getNextEntry()) != null) {
			String zipEntryName = zipEntry.getName();
			if(zipEntryName.equalsIgnoreCase(entryName)) {
				foundEntry = true;
				break;
			}
			zIn.closeEntry();
		}
		
		// If we didn't get it return
		if(foundEntry == false) {
			try{
				zIn.close();
			}
			catch(IOException ex) {}
			return null;
		}
		
		sb = new StringBuffer();
		
		// Extract it
		while((bit = zIn.read()) != -1) {
			sb.append((char)bit);
		}
		
		zIn.close();
		
		return sb.toString();
	}
	
	
	/**
	 * Extracts a named entry out of the zip file to the specified file
	 * Returns the File ref if OK or null if weirdness happens
	 * @param zipFile
	 * @param entryName
	 * @param outFile
	 * @return
	 * @throws IOException
	 */
	public static File extractZipEntry(File zipFile, String entryName, File outFile) throws IOException {
		ZipInputStream zIn;
		ZipEntry zipEntry;
		int bytesRead;
		final int bufSize = 1024;
		byte buf[] = new byte[bufSize];
		boolean foundEntry = false;
		
		// Ensure that the parent Folder exists
		if(!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
		zIn = new ZipInputStream(in);
		
		// Find the entry
		while((zipEntry = zIn.getNextEntry()) != null) {
			String zipEntryName = zipEntry.getName();
			if(zipEntryName.equalsIgnoreCase(entryName)) {
				foundEntry = true;
				break;
			}
			zIn.closeEntry();
		}
		
		// If we didn't get it return
		if(foundEntry == false) {
			return null;
		} 
		
		// Extract it and save to outFile
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile), bufSize);
		
		while((bytesRead = zIn.read(buf)) != -1) {
			out.write(buf, 0, bytesRead);
			try {
				// Allow other things to happen
				Thread.sleep(2);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		out.flush();
		out.close();
		zIn.close();
		
		// Restore time stamp
		outFile.setLastModified(zipEntry.getTime());
		
		return outFile;
	}
	
	/**
	 * Gets all file names out of a zip file in sorted name order
	 * Returns a list String names or null if none
	 * @param zipFile
	 * @return
	 * @throws IOException
	 */
	public static String[] getZipFileEntryNames(File zipFile) throws IOException {
		ZipInputStream zIn = null;
		ZipEntry zipEntry;
		final int bufSize = 1024;
		List<String> fileList = new ArrayList<String>();
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
		
		zIn = new ZipInputStream(in);
		
		try {
			while((zipEntry = zIn.getNextEntry()) != null) {
				// Don't add directories
				if(!zipEntry.isDirectory()) {
					String zipEntryName = zipEntry.getName();
					fileList.add(zipEntryName);
				}
				zIn.closeEntry();
			}
		}
		// We'll catch this exception to close the file otherwise it will remain locked
		catch(IOException ex) {
			zIn.close();
			throw ex;
		}
		finally {
            zIn.close();
        }
		
		String[] names = new String[fileList.size()];
		fileList.toArray(names);
		Arrays.sort(names);
		return names;
	}
	
	/**
	 * Extracts all entries out of the zip file to the specified folder
	 * Target folder is created if it doesn't exist
	 * @param zipFile
	 * @param targetFolder
	 * @throws IOException
	 */
	public static void unpackZip(File zipFile, File targetFolder) throws IOException {
		unpackZip(zipFile, targetFolder, null);
	}
	
	/**
	 * Extracts all entries out of the zip file to the specified folder
	 * Target folder is created if it doesn't exist
	 * Returns true or false to indicate if the progress cancel was pressed
	 * @param zipFile
	 * @param targetFolder
	 * @param progressMonitor an optional ProgressMonitor.  This can be null.
	 * @return false if the progressMonitor is cancelled
	 * @throws IOException If error or user cancelled
	 */
	public static void unpackZip(File zipFile, File targetFolder, IProgressMonitor progressMonitor) throws IOException {
	    targetFolder.mkdirs();
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		ZipInputStream zIn = null;
		ZipEntry zipEntry;
		int bytesRead;
		final int bufSize = 512;
		byte buf[] = new byte[bufSize];
		
		in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
		zIn = new ZipInputStream(in);
		
		try {
			while((zipEntry = zIn.getNextEntry()) != null) {
				// Don't add directories
				if(!zipEntry.isDirectory()) {
					File outFile = new File(targetFolder, zipEntry.getName());
					
					// Ensure that the parent Folder exists
					if(!outFile.getParentFile().exists()) {
					    outFile.getParentFile().mkdirs();
					}
					
					out = new BufferedOutputStream(new FileOutputStream(outFile), bufSize);
					
					// If we have a Progress Monitor, display name
					if(progressMonitor != null) {
					    progressMonitor.setTaskName(zipEntry.getName());
					}
					
					int sleep_count = 0;
					
					while((bytesRead = zIn.read(buf)) != -1) {
						out.write(buf, 0, bytesRead);
						
						// Allow other things to happen every 40 chunks
						if(sleep_count >= 40) {
							try {
								Thread.sleep(2);
							} catch(InterruptedException ex) {
								ex.printStackTrace();
							}
							sleep_count = 0;
						}
						
						sleep_count++;
						
						// If we have a Progress Monitor
						if(progressMonitor != null && progressMonitor.isCanceled()) {
							out.flush();
							out.close();
							zIn.close();
							throw new IOException("User Cancelled"); //$NON-NLS-1$
						}
					}
					
					// Restore time stamp
					outFile.setLastModified(zipEntry.getTime());
					
					// Close File
					out.flush();
					out.close();
				}
				
				zIn.closeEntry();
			}
			
			zIn.close();
			
		}
		// We'll catch this exception to close the file otherwise it remains locked
		catch(IOException ex) {
			zIn.close();
			if(out != null) {
				out.flush();
				out.close();
			}
			// And throw it again
			throw ex;
		}
	}
}