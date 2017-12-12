package com.dk.autotool.view;

import com.dk.autotool.controler.Controller;
import com.dk.autotool.model.HeaderFileModel;
import com.dk.autotool.model.NoteModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private final String mFileName = "autoToolConfig";
	private JRadioButton mJavaRadioButton;
	private JRadioButton mMKRadioButton;
	private JRadioButton mXmlRadioButton;
	private HeaderFileModel mHeaderFileModel;
	private Map<String, JTextField> mTextFieldMap;
	private JCheckBox mHeadCheckButton;
	private JCheckBox mModifyCheckButton;
	private JRadioButton mNoteJavaRadioButton;
	private JRadioButton mNoteMKRadioButton;
	private JRadioButton mNoteXmlRadioButton;
	private NoteModel mNoteModel;
	private Map<String, JTextField> mNoteTextFieldMap;
	private JRadioButton mNoteOneRadioButton;
	private JRadioButton mNoteTwoRadioButton;
	private JComboBox mJOperateCombobBox;
	private JComboBox mJtypeComboBox;
	private JButton mJButton;
	private JButton mNoteJButton;
	private String mResult;
	private Controller mController;
	private JTabbedPane mTabPane;
	private File mFile;

	public MainView() {
		this.mController = new Controller();
		this.mTextFieldMap = new HashMap();
		this.mNoteTextFieldMap = new HashMap();
		this.mHeaderFileModel = new HeaderFileModel();
		this.mNoteModel = new NoteModel();

		this.mTabPane = new JTabbedPane();
		initHander();
		initDescription();

		add(this.mTabPane);
		setTitle("工具");
		setBounds(300, 50, 500, 600);
		setResizable(false);
		setDefaultCloseOperation(2);

		setVisible(true);
	}

	private void initDescription() {
		JPanel panelDec = new JPanel();
		JPanel noteFramePanel = new JPanel();

		panelDec.setLayout(new GridLayout(2, 1));

		this.mNoteJavaRadioButton = new JRadioButton("java/c/c++");
		this.mNoteJavaRadioButton.setSelected(true);
		this.mNoteXmlRadioButton = new JRadioButton("xml");
		this.mNoteXmlRadioButton = new JRadioButton("xml");
		this.mNoteMKRadioButton = new JRadioButton("MK/shell");

		MyListener listener = new MyListener();
		this.mNoteJavaRadioButton.addActionListener(listener);
		this.mNoteXmlRadioButton.addActionListener(listener);
		this.mNoteMKRadioButton.addActionListener(listener);

		Class<NoteModel> noteModelClass = NoteModel.class;
		Field[] noteModelFileds = noteModelClass.getDeclaredFields();
		int fieldLen = noteModelFileds.length;

		Panel panel = new Panel();
		panel.setLayout(new GridLayout(fieldLen + 6, 2));

		panel.add(this.mNoteJavaRadioButton);
		panel.add(this.mNoteXmlRadioButton);
		panel.add(this.mNoteMKRadioButton);
		panel.add(new Label());

		this.mNoteOneRadioButton = new JRadioButton("one block");
		this.mNoteTwoRadioButton = new JRadioButton("two block");
		this.mNoteTwoRadioButton.setSelected(true);
		this.mNoteOneRadioButton.addActionListener(listener);
		this.mNoteTwoRadioButton.addActionListener(listener);

		panel.add(this.mNoteOneRadioButton);
		panel.add(this.mNoteTwoRadioButton);

		String[] types = { "BUGFIX", "FEATURE", "PLATFORM" };

		this.mJtypeComboBox = new JComboBox();
		for (int i = 0; i < types.length; i++) {
			this.mJtypeComboBox.addItem(types[i]);
		}
		this.mJtypeComboBox.setLightWeightPopupEnabled(false);

		JLabel comboBoxLabel = new JLabel("Change Type");
		panel.add(comboBoxLabel);
		panel.add(this.mJtypeComboBox);

		String[] operates = { "Add", "Mod", "Del" };

		this.mJOperateCombobBox = new JComboBox();
		for (int i = 0; i < operates.length; i++) {
			this.mJOperateCombobBox.addItem(operates[i]);
		}
		this.mJOperateCombobBox.setLightWeightPopupEnabled(false);
		JLabel opComboBoxLabel = new JLabel("Change Operate");
		panel.add(opComboBoxLabel);
		panel.add(this.mJOperateCombobBox);
		for (int i = 0; i < fieldLen; i++) {
			String content = null;
			String fieldName = noteModelFileds[i].getName();
			if (!fieldName.equals("sCaptionMap")) {
				Label label = new Label((String) NoteModel.sCaptionMap.get(fieldName));
				JTextField textField = new JTextField(20);
				if (fieldName.equals("mDate")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					content = sdf.format(new Date());
				}
				if (fieldName.equals("mAuthor")) {
					content = getFile("Author:");
				}
				textField.setText(content);
				panel.add(label);
				panel.add(textField);
				this.mNoteTextFieldMap.put(fieldName, textField);
			}
		}
		panelDec.add(panel);

		this.mNoteJButton = new JButton("生成");
		this.mNoteJButton.addActionListener(listener);

		Panel panel2 = new Panel();
		panel2.add(this.mNoteJButton);

		panelDec.add(panel2);
		noteFramePanel.add(panelDec);

		this.mTabPane.addTab("生成注释", null, panelDec, "生成注释");
	}

	private void initHander() {
		JPanel panelHander = new JPanel();
		JPanel handerFramePanel = new JPanel();

		panelHander.setLayout(new GridLayout(2, 1));

		this.mHeadCheckButton = new JCheckBox("生成头文件?");
		this.mHeadCheckButton.setSelected(true);
		this.mModifyCheckButton = new JCheckBox("生成修改注释");
		this.mModifyCheckButton.setSelected(true);

		this.mJavaRadioButton = new JRadioButton("java/c/c++");
		this.mJavaRadioButton.setSelected(true);
		this.mXmlRadioButton = new JRadioButton("xml");
		this.mMKRadioButton = new JRadioButton("MK/shell");

		MyListener listener = new MyListener();
		this.mJavaRadioButton.addActionListener(listener);
		this.mXmlRadioButton.addActionListener(listener);
		this.mMKRadioButton.addActionListener(listener);

		this.mHeadCheckButton.addActionListener(listener);
		this.mModifyCheckButton.addActionListener(listener);

		Class<HeaderFileModel> headerFileModelClass = HeaderFileModel.class;
		Field[] headerFileModelFileds = headerFileModelClass.getDeclaredFields();
		int fieldLen = headerFileModelFileds.length;

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(fieldLen + 3, 2));

		panel.add(this.mHeadCheckButton);
		panel.add(this.mModifyCheckButton);
		panel.add(this.mJavaRadioButton);
		panel.add(this.mXmlRadioButton);
		panel.add(this.mMKRadioButton);
		panel.add(new Label());
		for (int i = 0; i < fieldLen; i++) {
			String content = null;
			String fieldName = headerFileModelFileds[i].getName();
			if (!fieldName.equals("sCaptionMap")) {
				Label label = new Label((String) HeaderFileModel.sCaptionMap.get(fieldName));
				JTextField textField = new JTextField(20);
				if (fieldName.equals("mModifDate")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					content = sdf.format(new Date());
				}
				if (fieldName.equals("mDate")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
					content = sdf.format(new Date());
				}
				if (fieldName.equals("mAuthor")) {
					content = getFile("Author:");
				} else if (fieldName.equals("mEmail")) {
					content = getFile("Email:");
				}
				textField.setText(content);

				panel.add(label);
				panel.add(textField);
				this.mTextFieldMap.put(fieldName, textField);
			}
		}
		panelHander.add(panel);

		this.mJButton = new JButton("生成");
		this.mJButton.addActionListener(listener);

		JPanel panel2 = new JPanel();
		panel2.add(this.mJButton);

		panelHander.add(panel2);
		handerFramePanel.add(panelHander);
		this.mTabPane.addTab("生成头文件?", null, handerFramePanel, "生成头文件?");
	}

	/* Error */
	private String getFile(String string) {
		// Byte code:
		// 0: aload_0
		// 1: new 352 java/io/File
		// 4: dup
		// 5: ldc 12
		// 7: invokespecial 354 java/io/File:<init> (Ljava/lang/String;)V
		// 10: putfield 355 com/tct/autotool/view/MainView:mFile Ljava/io/File;
		// 13: aload_0
		// 14: getfield 355 com/tct/autotool/view/MainView:mFile Ljava/io/File;
		// 17: invokevirtual 357 java/io/File:exists ()Z
		// 20: ifne +5 -> 25
		// 23: aconst_null
		// 24: areturn
		// 25: aconst_null
		// 26: astore_2
		// 27: aconst_null
		// 28: astore_3
		// 29: aconst_null
		// 30: astore 4
		// 32: new 361 java/util/Scanner
		// 35: dup
		// 36: aload_0
		// 37: getfield 355 com/tct/autotool/view/MainView:mFile Ljava/io/File;
		// 40: invokespecial 363 java/util/Scanner:<init> (Ljava/io/File;)V
		// 43: astore_2
		// 44: goto +33 -> 77
		// 47: aload_2
		// 48: invokevirtual 366 java/util/Scanner:nextLine ()Ljava/lang/String;
		// 51: astore_3
		// 52: aload_3
		// 53: aload_1
		// 54: invokevirtual 369 java/lang/String:startsWith (Ljava/lang/String;)Z
		// 57: ifeq +20 -> 77
		// 60: aload_3
		// 61: aload_1
		// 62: invokevirtual 373 java/lang/String:length ()I
		// 65: aload_3
		// 66: invokevirtual 373 java/lang/String:length ()I
		// 69: invokevirtual 377 java/lang/String:substring (II)Ljava/lang/String;
		// 72: astore 4
		// 74: goto +44 -> 118
		// 77: aload_2
		// 78: invokevirtual 381 java/util/Scanner:hasNextLine ()Z
		// 81: ifne -34 -> 47
		// 84: goto +34 -> 118
		// 87: astore 5
		// 89: aload 5
		// 91: invokevirtual 384 java/io/FileNotFoundException:printStackTrace ()V
		// 94: aload_2
		// 95: ifnull +31 -> 126
		// 98: aload_2
		// 99: invokevirtual 389 java/util/Scanner:close ()V
		// 102: goto +24 -> 126
		// 105: astore 6
		// 107: aload_2
		// 108: ifnull +7 -> 115
		// 111: aload_2
		// 112: invokevirtual 389 java/util/Scanner:close ()V
		// 115: aload 6
		// 117: athrow
		// 118: aload_2
		// 119: ifnull +7 -> 126
		// 122: aload_2
		// 123: invokevirtual 389 java/util/Scanner:close ()V
		// 126: aload 4
		// 128: areturn
		// Line number table:
		// Java source line #309 -> byte code offset #0
		// Java source line #310 -> byte code offset #13
		// Java source line #311 -> byte code offset #23
		// Java source line #313 -> byte code offset #25
		// Java source line #314 -> byte code offset #27
		// Java source line #315 -> byte code offset #29
		// Java source line #317 -> byte code offset #32
		// Java source line #318 -> byte code offset #44
		// Java source line #319 -> byte code offset #47
		// Java source line #320 -> byte code offset #52
		// Java source line #321 -> byte code offset #60
		// Java source line #322 -> byte code offset #74
		// Java source line #318 -> byte code offset #77
		// Java source line #325 -> byte code offset #84
		// Java source line #326 -> byte code offset #89
		// Java source line #328 -> byte code offset #94
		// Java source line #329 -> byte code offset #98
		// Java source line #327 -> byte code offset #105
		// Java source line #328 -> byte code offset #107
		// Java source line #329 -> byte code offset #111
		// Java source line #331 -> byte code offset #115
		// Java source line #328 -> byte code offset #118
		// Java source line #329 -> byte code offset #122
		// Java source line #332 -> byte code offset #126
		// Local variable table:
		// start length slot name signature
		// 0 129 0 this MainView
		// 0 129 1 string String
		// 26 97 2 scanner java.util.Scanner
		// 28 38 3 tmp String
		// 30 97 4 result String
		// 87 3 5 e java.io.FileNotFoundException
		// 105 11 6 localObject Object
		// Exception table:
		// from to target type
		// 32 84 87 java/io/FileNotFoundException
		// 32 94 105 finally
		return null;
	}

	public class MyListener implements ActionListener {
		public MyListener() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(MainView.this.mJavaRadioButton)) {
				MainView.this.mXmlRadioButton.setSelected(false);
				MainView.this.mMKRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mXmlRadioButton)) {
				MainView.this.mJavaRadioButton.setSelected(false);
				MainView.this.mMKRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mMKRadioButton)) {
				MainView.this.mJavaRadioButton.setSelected(false);
				MainView.this.mXmlRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mNoteXmlRadioButton)) {
				MainView.this.mNoteJavaRadioButton.setSelected(false);
				MainView.this.mNoteMKRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mNoteJavaRadioButton)) {
				MainView.this.mNoteXmlRadioButton.setSelected(false);
				MainView.this.mNoteMKRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mNoteMKRadioButton)) {
				MainView.this.mNoteXmlRadioButton.setSelected(false);
				MainView.this.mNoteJavaRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mNoteOneRadioButton)) {
				MainView.this.mNoteTwoRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mNoteTwoRadioButton)) {
				MainView.this.mNoteOneRadioButton.setSelected(false);
			} else if (e.getSource().equals(MainView.this.mHeadCheckButton)) {
				if (MainView.this.mHeadCheckButton.isSelected()) {
					((JTextField) MainView.this.mTextFieldMap.get("mDate")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mRole")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mReferenceDoc")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mComments")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mFile")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mLabels")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mEmail")).setVisible(true);
				} else {
					((JTextField) MainView.this.mTextFieldMap.get("mDate")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mRole")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mReferenceDoc")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mComments")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mFile")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mLabels")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mEmail")).setVisible(false);
				}
			} else if (e.getSource().equals(MainView.this.mModifyCheckButton)) {
				if (MainView.this.mModifyCheckButton.isSelected()) {
					((JTextField) MainView.this.mTextFieldMap.get("mModifDate")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mKey")).setVisible(true);
					((JTextField) MainView.this.mTextFieldMap.get("mComment")).setVisible(true);
				} else {
					((JTextField) MainView.this.mTextFieldMap.get("mModifDate")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mKey")).setVisible(false);
					((JTextField) MainView.this.mTextFieldMap.get("mComment")).setVisible(false);
				}
			} else {
				Object localObject;
				if (e.getSource().equals(MainView.this.mJButton)) {
					Class<HeaderFileModel> headerFileModelClass = HeaderFileModel.class;
					Field[] headerFileModelFileds = headerFileModelClass.getDeclaredFields();
					int fieldLen = headerFileModelFileds.length;

					Class<String> argTypes = String.class;
					for (int i = 0; i < fieldLen; i++) {
						String fieldName = headerFileModelFileds[i].getName();
						if (!fieldName.equals("sCaptionMap")) {
							try {
								String temp = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
								Method method = headerFileModelClass.getMethod("set" + temp, new Class[] { argTypes });
								String tmp = ((JTextField) MainView.this.mTextFieldMap.get(fieldName)).getText()
										.toString();
								if (tmp.length() == 0) {
									if (fieldName.equals("mAuthor")) {
										JOptionPane.showConfirmDialog(null, "请输入Author", "输入Author", -1);
										return;
									}
									tmp = " ";
								}
								localObject = method.invoke(MainView.this.mHeaderFileModel, new Object[] { tmp });
							} catch (IllegalArgumentException e1) {
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (SecurityException e1) {
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
						}
					}
					if ((!MainView.this.mHeadCheckButton.isSelected())
							&& (!MainView.this.mModifyCheckButton.isSelected())) {
						JOptionPane.showConfirmDialog(null, "请选择文件种类", "选择种类", -1);
						return;
					}
					if ((!MainView.this.mXmlRadioButton.isSelected()) && (!MainView.this.mJavaRadioButton.isSelected())
							&& (!MainView.this.mMKRadioButton.isSelected())) {
						JOptionPane.showConfirmDialog(null, "请选择生成文件类型", "文件类型", -1);
						return;
					}
					int category = 2;
					if (!MainView.this.mHeadCheckButton.isSelected()) {
						category = 1;
					}
					if (!MainView.this.mModifyCheckButton.isSelected()) {
						category = 0;
					}
					if ((category != 0) && (((JTextField) MainView.this.mTextFieldMap.get("mComment")).getText()
							.toString().length() == 0)) {
						JOptionPane.showConfirmDialog(null, "Modify Comment(what, where, why)不能为空", "不能为空", -1);
						return;
					}
					String type = "java";
					if (MainView.this.mXmlRadioButton.isSelected()) {
						type = "xml";
					} else if (MainView.this.mMKRadioButton.isSelected()) {
						type = "mk";
					}
					MainView.this.mResult = MainView.this.mController.getHeader(category, type,
							MainView.this.mHeaderFileModel);
					createConfigFile(MainView.this.mHeaderFileModel.getMAuthor(),
							MainView.this.mHeaderFileModel.getMEmail());
					createResultFrame();
				} else if (e.getSource().equals(MainView.this.mNoteJButton)) {
					Class<NoteModel> noteModelClass = NoteModel.class;
					Field[] noteModelFileds = noteModelClass.getDeclaredFields();
					int fieldLen = noteModelFileds.length;

					Class<String> argTypes = String.class;
					for (int i = 0; i < fieldLen; i++) {
						String fieldName = noteModelFileds[i].getName();
						if (!fieldName.equals("sCaptionMap")) {
							try {
								String temp = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
								Method method = noteModelClass.getMethod("set" + temp, new Class[] { argTypes });
								String tmp = ((JTextField) MainView.this.mNoteTextFieldMap.get(fieldName)).getText()
										.toString();
								if ((tmp.length() == 0)
										&& ((fieldName.equals("mAuthor")) || (fieldName.equals("mDate")))) {
									JOptionPane.showConfirmDialog(null,
											(String) NoteModel.sCaptionMap.get(fieldName) + "不能为空", "不能为空", -1);
									return;
								}
								localObject = method.invoke(MainView.this.mNoteModel, new Object[] { tmp });
							} catch (IllegalArgumentException e1) {
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (SecurityException e1) {
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
						}
					}
					if ((!MainView.this.mNoteXmlRadioButton.isSelected())
							&& (!MainView.this.mNoteJavaRadioButton.isSelected())
							&& (!MainView.this.mNoteMKRadioButton.isSelected())) {
						JOptionPane.showConfirmDialog(null, "请选择生成文件类型", "选择类型", -1);
						return;
					}
					if ((!MainView.this.mNoteOneRadioButton.isSelected())
							&& (!MainView.this.mNoteTwoRadioButton.isSelected())) {
						JOptionPane.showConfirmDialog(null, "请选择生成行数", "选择行数", -1);
						return;
					}
					String type = "java";
					if (MainView.this.mNoteXmlRadioButton.isSelected()) {
						type = "xml";
					} else if (MainView.this.mNoteMKRadioButton.isSelected()) {
						type = "mk";
					}
					int lineNum = 1;
					if (MainView.this.mNoteTwoRadioButton.isSelected()) {
						lineNum = 2;
					}
					MainView.this.mResult = MainView.this.mController.getNote(
							MainView.this.mJtypeComboBox.getSelectedItem().toString(),
							MainView.this.mJOperateCombobBox.getSelectedItem().toString(), lineNum, type,
							MainView.this.mNoteModel);
					createConfigFile(MainView.this.mNoteModel.getMAuthor(), null);
					createResultFrame();
				}
			}
		}

		/* Error */
		private void createConfigFile(String mAuthor, String mEmail) {
			// Byte code:
			// 0: aconst_null
			// 1: astore_3
			// 2: aconst_null
			// 3: astore 4
			// 5: aload_0
			// 6: getfield 12 com/tct/autotool/view/MainView$MyListener:this$0
			// Lcom/tct/autotool/view/MainView;
			// 9: new 334 java/io/File
			// 12: dup
			// 13: ldc_w 336
			// 16: invokespecial 338 java/io/File:<init> (Ljava/lang/String;)V
			// 19: invokestatic 339 com/tct/autotool/view/MainView:access$20
			// (Lcom/tct/autotool/view/MainView;Ljava/io/File;)V
			// 22: aload_0
			// 23: getfield 12 com/tct/autotool/view/MainView$MyListener:this$0
			// Lcom/tct/autotool/view/MainView;
			// 26: invokestatic 343 com/tct/autotool/view/MainView:access$21
			// (Lcom/tct/autotool/view/MainView;)Ljava/io/File;
			// 29: invokevirtual 347 java/io/File:exists ()Z
			// 32: ifne +14 -> 46
			// 35: aload_0
			// 36: getfield 12 com/tct/autotool/view/MainView$MyListener:this$0
			// Lcom/tct/autotool/view/MainView;
			// 39: invokestatic 343 com/tct/autotool/view/MainView:access$21
			// (Lcom/tct/autotool/view/MainView;)Ljava/io/File;
			// 42: invokevirtual 350 java/io/File:createNewFile ()Z
			// 45: pop
			// 46: new 353 java/util/Scanner
			// 49: dup
			// 50: aload_0
			// 51: getfield 12 com/tct/autotool/view/MainView$MyListener:this$0
			// Lcom/tct/autotool/view/MainView;
			// 54: invokestatic 343 com/tct/autotool/view/MainView:access$21
			// (Lcom/tct/autotool/view/MainView;)Ljava/io/File;
			// 57: invokespecial 355 java/util/Scanner:<init> (Ljava/io/File;)V
			// 60: astore 5
			// 62: aconst_null
			// 63: astore 6
			// 65: aconst_null
			// 66: astore 7
			// 68: aconst_null
			// 69: astore 8
			// 71: aload 5
			// 73: invokevirtual 358 java/util/Scanner:hasNextLine ()Z
			// 76: ifeq +83 -> 159
			// 79: aload 5
			// 81: invokevirtual 361 java/util/Scanner:nextLine ()Ljava/lang/String;
			// 84: astore 8
			// 86: aload 8
			// 88: ldc_w 364
			// 91: invokevirtual 366 java/lang/String:startsWith (Ljava/lang/String;)Z
			// 94: ifeq +21 -> 115
			// 97: aload 8
			// 99: ldc_w 364
			// 102: invokevirtual 173 java/lang/String:length ()I
			// 105: aload 8
			// 107: invokevirtual 173 java/lang/String:length ()I
			// 110: invokevirtual 139 java/lang/String:substring (II)Ljava/lang/String;
			// 113: astore 6
			// 115: aload 5
			// 117: invokevirtual 358 java/util/Scanner:hasNextLine ()Z
			// 120: ifeq +39 -> 159
			// 123: aload 5
			// 125: invokevirtual 361 java/util/Scanner:nextLine ()Ljava/lang/String;
			// 128: astore 8
			// 130: aload 8
			// 132: ldc_w 370
			// 135: invokevirtual 366 java/lang/String:startsWith (Ljava/lang/String;)Z
			// 138: ifeq +21 -> 159
			// 141: aload 8
			// 143: ldc_w 370
			// 146: invokevirtual 173 java/lang/String:length ()I
			// 149: aload 8
			// 151: invokevirtual 173 java/lang/String:length ()I
			// 154: invokevirtual 139 java/lang/String:substring (II)Ljava/lang/String;
			// 157: astore 7
			// 159: aload 5
			// 161: invokevirtual 372 java/util/Scanner:close ()V
			// 164: new 375 java/io/FileOutputStream
			// 167: dup
			// 168: aload_0
			// 169: getfield 12 com/tct/autotool/view/MainView$MyListener:this$0
			// Lcom/tct/autotool/view/MainView;
			// 172: invokestatic 343 com/tct/autotool/view/MainView:access$21
			// (Lcom/tct/autotool/view/MainView;)Ljava/io/File;
			// 175: invokespecial 377 java/io/FileOutputStream:<init> (Ljava/io/File;)V
			// 178: astore_3
			// 179: new 378 java/io/PrintWriter
			// 182: dup
			// 183: aload_3
			// 184: invokespecial 380 java/io/PrintWriter:<init> (Ljava/io/OutputStream;)V
			// 187: astore 4
			// 189: aload_1
			// 190: ifnull +28 -> 218
			// 193: aload 4
			// 195: new 137 java/lang/StringBuilder
			// 198: dup
			// 199: ldc_w 364
			// 202: invokespecial 150 java/lang/StringBuilder:<init> (Ljava/lang/String;)V
			// 205: aload_1
			// 206: invokevirtual 156 java/lang/StringBuilder:append
			// (Ljava/lang/String;)Ljava/lang/StringBuilder;
			// 209: invokevirtual 160 java/lang/StringBuilder:toString ()Ljava/lang/String;
			// 212: invokevirtual 383 java/io/PrintWriter:println (Ljava/lang/String;)V
			// 215: goto +26 -> 241
			// 218: aload 4
			// 220: new 137 java/lang/StringBuilder
			// 223: dup
			// 224: ldc_w 364
			// 227: invokespecial 150 java/lang/StringBuilder:<init> (Ljava/lang/String;)V
			// 230: aload 6
			// 232: invokevirtual 156 java/lang/StringBuilder:append
			// (Ljava/lang/String;)Ljava/lang/StringBuilder;
			// 235: invokevirtual 160 java/lang/StringBuilder:toString ()Ljava/lang/String;
			// 238: invokevirtual 383 java/io/PrintWriter:println (Ljava/lang/String;)V
			// 241: aload_2
			// 242: ifnull +28 -> 270
			// 245: aload 4
			// 247: new 137 java/lang/StringBuilder
			// 250: dup
			// 251: ldc_w 370
			// 254: invokespecial 150 java/lang/StringBuilder:<init> (Ljava/lang/String;)V
			// 257: aload_2
			// 258: invokevirtual 156 java/lang/StringBuilder:append
			// (Ljava/lang/String;)Ljava/lang/StringBuilder;
			// 261: invokevirtual 160 java/lang/StringBuilder:toString ()Ljava/lang/String;
			// 264: invokevirtual 383 java/io/PrintWriter:println (Ljava/lang/String;)V
			// 267: goto +118 -> 385
			// 270: aload 4
			// 272: new 137 java/lang/StringBuilder
			// 275: dup
			// 276: ldc_w 370
			// 279: invokespecial 150 java/lang/StringBuilder:<init> (Ljava/lang/String;)V
			// 282: aload 7
			// 284: invokevirtual 156 java/lang/StringBuilder:append
			// (Ljava/lang/String;)Ljava/lang/StringBuilder;
			// 287: invokevirtual 160 java/lang/StringBuilder:toString ()Ljava/lang/String;
			// 290: invokevirtual 383 java/io/PrintWriter:println (Ljava/lang/String;)V
			// 293: goto +92 -> 385
			// 296: astore 5
			// 298: aload 5
			// 300: invokevirtual 386 java/io/IOException:printStackTrace ()V
			// 303: aload 4
			// 305: ifnull +13 -> 318
			// 308: aload 4
			// 310: invokevirtual 389 java/io/PrintWriter:flush ()V
			// 313: aload 4
			// 315: invokevirtual 392 java/io/PrintWriter:close ()V
			// 318: aload_3
			// 319: ifnull +103 -> 422
			// 322: aload_3
			// 323: invokevirtual 393 java/io/FileOutputStream:flush ()V
			// 326: aload_3
			// 327: invokevirtual 394 java/io/FileOutputStream:close ()V
			// 330: goto +92 -> 422
			// 333: astore 10
			// 335: aload 10
			// 337: invokevirtual 386 java/io/IOException:printStackTrace ()V
			// 340: goto +82 -> 422
			// 343: astore 9
			// 345: aload 4
			// 347: ifnull +13 -> 360
			// 350: aload 4
			// 352: invokevirtual 389 java/io/PrintWriter:flush ()V
			// 355: aload 4
			// 357: invokevirtual 392 java/io/PrintWriter:close ()V
			// 360: aload_3
			// 361: ifnull +21 -> 382
			// 364: aload_3
			// 365: invokevirtual 393 java/io/FileOutputStream:flush ()V
			// 368: aload_3
			// 369: invokevirtual 394 java/io/FileOutputStream:close ()V
			// 372: goto +10 -> 382
			// 375: astore 10
			// 377: aload 10
			// 379: invokevirtual 386 java/io/IOException:printStackTrace ()V
			// 382: aload 9
			// 384: athrow
			// 385: aload 4
			// 387: ifnull +13 -> 400
			// 390: aload 4
			// 392: invokevirtual 389 java/io/PrintWriter:flush ()V
			// 395: aload 4
			// 397: invokevirtual 392 java/io/PrintWriter:close ()V
			// 400: aload_3
			// 401: ifnull +21 -> 422
			// 404: aload_3
			// 405: invokevirtual 393 java/io/FileOutputStream:flush ()V
			// 408: aload_3
			// 409: invokevirtual 394 java/io/FileOutputStream:close ()V
			// 412: goto +10 -> 422
			// 415: astore 10
			// 417: aload 10
			// 419: invokevirtual 386 java/io/IOException:printStackTrace ()V
			// 422: return
			// Line number table:
			// Java source line #544 -> byte code offset #0
			// Java source line #545 -> byte code offset #2
			// Java source line #547 -> byte code offset #5
			// Java source line #548 -> byte code offset #22
			// Java source line #549 -> byte code offset #35
			// Java source line #551 -> byte code offset #46
			// Java source line #552 -> byte code offset #62
			// Java source line #553 -> byte code offset #65
			// Java source line #554 -> byte code offset #68
			// Java source line #555 -> byte code offset #71
			// Java source line #556 -> byte code offset #79
			// Java source line #557 -> byte code offset #86
			// Java source line #558 -> byte code offset #97
			// Java source line #560 -> byte code offset #115
			// Java source line #561 -> byte code offset #123
			// Java source line #562 -> byte code offset #130
			// Java source line #563 -> byte code offset #141
			// Java source line #567 -> byte code offset #159
			// Java source line #568 -> byte code offset #164
			// Java source line #569 -> byte code offset #179
			// Java source line #570 -> byte code offset #189
			// Java source line #571 -> byte code offset #193
			// Java source line #572 -> byte code offset #215
			// Java source line #573 -> byte code offset #218
			// Java source line #575 -> byte code offset #241
			// Java source line #576 -> byte code offset #245
			// Java source line #577 -> byte code offset #267
			// Java source line #578 -> byte code offset #270
			// Java source line #580 -> byte code offset #293
			// Java source line #581 -> byte code offset #298
			// Java source line #583 -> byte code offset #303
			// Java source line #584 -> byte code offset #308
			// Java source line #585 -> byte code offset #313
			// Java source line #587 -> byte code offset #318
			// Java source line #589 -> byte code offset #322
			// Java source line #590 -> byte code offset #326
			// Java source line #591 -> byte code offset #330
			// Java source line #592 -> byte code offset #335
			// Java source line #582 -> byte code offset #343
			// Java source line #583 -> byte code offset #345
			// Java source line #584 -> byte code offset #350
			// Java source line #585 -> byte code offset #355
			// Java source line #587 -> byte code offset #360
			// Java source line #589 -> byte code offset #364
			// Java source line #590 -> byte code offset #368
			// Java source line #591 -> byte code offset #372
			// Java source line #592 -> byte code offset #377
			// Java source line #595 -> byte code offset #382
			// Java source line #583 -> byte code offset #385
			// Java source line #584 -> byte code offset #390
			// Java source line #585 -> byte code offset #395
			// Java source line #587 -> byte code offset #400
			// Java source line #589 -> byte code offset #404
			// Java source line #590 -> byte code offset #408
			// Java source line #591 -> byte code offset #412
			// Java source line #592 -> byte code offset #417
			// Java source line #596 -> byte code offset #422
			// Local variable table:
			// start length slot name signature
			// 0 423 0 this MyListener
			// 0 423 1 mAuthor String
			// 0 423 2 mEmail String
			// 1 408 3 fileOutputStream java.io.FileOutputStream
			// 3 393 4 printWriter java.io.PrintWriter
			// 60 100 5 scanner java.util.Scanner
			// 296 3 5 e java.io.IOException
			// 63 168 6 oAuthor String
			// 66 217 7 oEmail String
			// 69 81 8 tmp String
			// 343 40 9 localObject Object
			// 333 3 10 e java.io.IOException
			// 375 3 10 e java.io.IOException
			// 415 3 10 e java.io.IOException
			// Exception table:
			// from to target type
			// 5 293 296 java/io/IOException
			// 322 330 333 java/io/IOException
			// 5 303 343 finally
			// 364 372 375 java/io/IOException
			// 404 412 415 java/io/IOException
		}

		private void createResultFrame() {
			JFrame resultJFrame = new JFrame();
			resultJFrame.setLayout(new BorderLayout());
			resultJFrame.setBounds(200, 50, 650, 600);
			JPanel jPanel = new JPanel();
			JButton copyButton = new JButton("copy all");
			jPanel.add(copyButton);

			MainView.this.mController.createRecord();
			if (MainView.this.mController.hasRecord()) {
				JButton copyRecordButton = new JButton("copy record");
				jPanel.add(copyRecordButton);
				copyRecordButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						Transferable text = new StringSelection(MainView.this.mController.getRecord());
						clipboard.setContents(text, null);
					}
				});
			}
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			editorPane.setContentType("text/html");
			editorPane.setText(MainView.this.mController.getHtml(MainView.this.mResult));

			JScrollPane jScrollPane = new JScrollPane(editorPane);
			resultJFrame.add(jPanel, "South");
			resultJFrame.add(jScrollPane, "Center");
			copyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					Transferable text = new StringSelection(MainView.this.mResult);
					clipboard.setContents(text, null);
				}
			});
			resultJFrame.setVisible(true);
		}
	}
}
