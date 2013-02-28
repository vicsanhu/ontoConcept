I have gathered and combined the classes needed to run the Rich Text Plugin from the following plugins into one:

org.eclipse.epf.common
org.eclipse.epf.richtext
org.eclipse.epf.ui


Hacked changes are tagged with "!!!PB HACK!!!" :

HTMLFormatter.java, line 136 - Remove <?xml version="1.0"?>
HTMLFormatter.java, line 191 - Get all html not just body

RichText.java, line 1245, - KeyUp
