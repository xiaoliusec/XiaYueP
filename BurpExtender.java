package burp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender.class */
public class BurpExtender extends AbstractTableModel implements IBurpExtender, ITab, IHttpListener, IScannerCheck, IMessageEditorController {
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JSplitPane splitPane;
    private IMessageEditor requestViewer;
    private IMessageEditor responseViewer;
    private IMessageEditor requestViewer_1;
    private IMessageEditor responseViewer_1;
    private IMessageEditor requestViewer_2;
    private IMessageEditor responseViewer_2;
    private IHttpRequestResponse currentlyDisplayedItem;
    private IHttpRequestResponse currentlyDisplayedItem_1;
    private IHttpRequestResponse currentlyDisplayedItem_2;
    public PrintWriter stdout;
    JTabbedPane tabs;
    int original_data_len;
    String temp_data;
    Table logTable;
    private final List<LogEntry> log = new ArrayList();
    private final List<Request_md5> log4_md5 = new ArrayList();
    int switchs = 0;
    int conut = 0;
    int select_row = 0;
    String white_URL = "";
    int white_switchs = 0;
    String data_1 = "";
    String data_2 = "";
    String universal_cookie = "";
    String xy_version = "1.5";
    int dedupe_mode = 0;

    @Override // burp.IBurpExtender
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks iBurpExtenderCallbacks) {
        this.stdout = new PrintWriter(iBurpExtenderCallbacks.getStdout(), true);
        this.stdout.println("hello xia Yue!");
        this.stdout.println("你好 欢迎使用 瞎越!");
        this.stdout.println("version:" + this.xy_version);
        this.callbacks = iBurpExtenderCallbacks;
        this.helpers = iBurpExtenderCallbacks.getHelpers();
        iBurpExtenderCallbacks.setExtensionName("xia Yue V" + this.xy_version);
        SwingUtilities.invokeLater(new Runnable() { // from class: burp.BurpExtender.1
            @Override // java.lang.Runnable
            public void run() {
                BurpExtender.this.splitPane = new JSplitPane(1);
                JSplitPane jSplitPane = new JSplitPane(0);
                JSplitPane jSplitPane2 = new JSplitPane(0);
                BurpExtender burpExtender = BurpExtender.this;
                BurpExtender burpExtender2 = BurpExtender.this;
                Objects.requireNonNull(burpExtender2);
                burpExtender.logTable = burpExtender2.new Table(BurpExtender.this);
                BurpExtender.this.logTable.setAutoCreateRowSorter(true);
                BurpExtender.this.logTable.getColumnModel().getColumn(0).setPreferredWidth(40);
                BurpExtender.this.logTable.getColumnModel().getColumn(0).setMaxWidth(50);
                BurpExtender.this.logTable.getColumnModel().getColumn(1).setPreferredWidth(40);
                BurpExtender.this.logTable.getColumnModel().getColumn(1).setMaxWidth(60);
                BurpExtender.this.logTable.getColumnModel().getColumn(2).setPreferredWidth(50);
                BurpExtender.this.logTable.getColumnModel().getColumn(3).setPreferredWidth(300);
                DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
                defaultTableCellRenderer.setHorizontalAlignment(2);
                for (int i = 1; i < BurpExtender.this.logTable.getColumnCount(); i++) {
                    BurpExtender.this.logTable.getColumnModel().getColumn(i).setCellRenderer(defaultTableCellRenderer);
                }
                BurpExtender.this.logTable.getColumnModel().getColumn(0).setHeaderRenderer(BurpExtender.this.new HeaderRenderer());
                final TableCellRenderer defaultHeaderRenderer = BurpExtender.this.logTable.getTableHeader().getDefaultRenderer();
                for (int i = 2; i < BurpExtender.this.logTable.getColumnCount(); i++) {
                    BurpExtender.this.logTable.getColumnModel().getColumn(i).setHeaderRenderer(new TableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            Component c = defaultHeaderRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            if (c instanceof JLabel) {
                                ((JLabel) c).setHorizontalAlignment(2);
                            }
                            return c;
                        }
                    });
                }
                BurpExtender.this.logTable.getTableHeader().addMouseListener(new MouseAdapter() { // from class: burp.BurpExtender.1.1
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (BurpExtender.this.logTable.columnAtPoint(mouseEvent.getPoint()) == 0) {
                            synchronized (BurpExtender.this.log) {
                                if (BurpExtender.this.log.isEmpty()) {
                                    return;
                                }
                                int i2 = 0;
                                Iterator<LogEntry> it = BurpExtender.this.log.iterator();
                                while (it.hasNext()) {
                                    if (it.next().selected) {
                                        i2++;
                                    }
                                }
                                boolean z = i2 != BurpExtender.this.log.size();
                                Iterator<LogEntry> it2 = BurpExtender.this.log.iterator();
                                while (it2.hasNext()) {
                                    it2.next().selected = z;
                                }
                                BurpExtender.this.fireTableDataChanged();
                            }
                        }
                    }
                });
                JScrollPane jScrollPane = new JScrollPane(BurpExtender.this.logTable);
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new GridLayout(1, 1));
                jPanel.add(jScrollPane);
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new GridLayout(0, 1, 5, 5));
                JLabel jLabel = new JLabel("插件名：瞎越 author：算命縖子");
                JLabel jLabel2 = new JLabel("吐司:www.t00ls.com");
                JLabel jLabel3 = new JLabel("版本：xia Yue V" + BurpExtender.this.xy_version);
                final JCheckBox jCheckBox = new JCheckBox("启动插件");
                final JCheckBox jCheckBox2 = new JCheckBox("启动万能cookie");
                final JCheckBox jCheckBox3 = new JCheckBox("全包MD5去重");
                JLabel jLabel5 = new JLabel("如果需要多个域名加白请用,隔开");
                final JTextField jTextField = new JTextField("填写白名单域名");
                JButton jButton = new JButton("清空列表");
                JButton jButton2 = new JButton("导出结果(CSV)");
                final JButton jButton3 = new JButton("启动白名单");
                JPanel jPanel3 = new JPanel();
                JLabel jLabel6 = new JLabel("越权：填写低权限认证信息,将会替换或新增");
                final JTextArea jTextArea = new JTextArea("Cookie: JSESSIONID=test;UUID=1; userid=admin\nAuthorization: Bearer test", 5, 30);
                JScrollPane jScrollPane2 = new JScrollPane(jTextArea);
                JLabel jLabel7 = new JLabel("未授权：将移除下列头部认证信息,区分大小写");
                final JTextArea jTextArea2 = new JTextArea("Cookie\nAuthorization\nToken", 5, 30);
                JScrollPane jScrollPane3 = new JScrollPane(jTextArea2);
                jPanel3.add(jLabel6);
                jPanel3.add(jScrollPane2);
                jPanel3.add(jLabel7);
                jPanel3.add(jScrollPane3);
                jPanel3.setLayout(new GridLayout(5, 1, 0, 0));
                jCheckBox.addItemListener(new ItemListener() { // from class: burp.BurpExtender.1.2
                    public void itemStateChanged(ItemEvent itemEvent) {
                        if (jCheckBox.isSelected()) {
                            BurpExtender.this.switchs = 1;
                            BurpExtender.this.data_1 = jTextArea.getText();
                            BurpExtender.this.data_2 = jTextArea2.getText();
                            jTextArea.setForeground(Color.BLACK);
                            jTextArea.setBackground(Color.LIGHT_GRAY);
                            jTextArea.setEditable(false);
                            jTextArea2.setForeground(Color.BLACK);
                            jTextArea2.setBackground(Color.LIGHT_GRAY);
                            jTextArea2.setEditable(false);
                            return;
                        }
                        BurpExtender.this.switchs = 0;
                        jTextArea.setForeground(Color.BLACK);
                        jTextArea.setBackground(Color.WHITE);
                        jTextArea.setEditable(true);
                        jTextArea2.setForeground(Color.BLACK);
                        jTextArea2.setBackground(Color.WHITE);
                        jTextArea2.setEditable(true);
                    }
                });
                jCheckBox2.addItemListener(new ItemListener() { // from class: burp.BurpExtender.1.3
                    public void itemStateChanged(ItemEvent itemEvent) {
                        if (jCheckBox2.isSelected()) {
                            BurpExtender.this.universal_cookie = "";
                        } else {
                            BurpExtender.this.universal_cookie = "";
                        }
                    }
                });
                jCheckBox3.addItemListener(new ItemListener() { // from class: burp.BurpExtender.1.4
                    public void itemStateChanged(ItemEvent itemEvent) {
                        if (jCheckBox3.isSelected()) {
                            BurpExtender.this.dedupe_mode = 1;
                        } else {
                            BurpExtender.this.dedupe_mode = 0;
                        }
                    }
                });
                jButton.addActionListener(new ActionListener() { // from class: burp.BurpExtender.1.5
                    public void actionPerformed(ActionEvent actionEvent) {
                        synchronized (BurpExtender.this.log) {
                            BurpExtender.this.log.clear();
                            BurpExtender.this.conut = 0;
                            BurpExtender.this.log4_md5.clear();
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                BurpExtender.this.fireTableDataChanged();
                            }
                        });
                    }
                });
                jButton2.addActionListener(new ActionListener() { // from class: burp.BurpExtender.1.6
                    public void actionPerformed(ActionEvent actionEvent) {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setDialogTitle("选择导出位置");
                        jFileChooser.setSelectedFile(new File("xia_yue_results_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
                        if (jFileChooser.showSaveDialog((Component) null) == 0) {
                            try {
                                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(jFileChooser.getSelectedFile()), StandardCharsets.UTF_8));
                                try {
                                    printWriter.write(65279);
                                    printWriter.println("#,类型,URL,原始长度,低权限长度,未授权长度,检测结论");
                                    synchronized (BurpExtender.this.log) {
                                        ArrayList<LogEntry> arrayList = new ArrayList();
                                        for (LogEntry logEntry : BurpExtender.this.log) {
                                            if (logEntry.selected) {
                                                arrayList.add(logEntry);
                                            }
                                        }
                                        if (arrayList.isEmpty()) {
                                            arrayList.addAll(BurpExtender.this.log);
                                        }
                                        for (LogEntry logEntry2 : arrayList) {
                                            Object obj = "正常";
                                            if (logEntry2.low_len.contains("✔") || logEntry2.Unauthorized_len.contains("✔")) {
                                                obj = "可能存在越权";
                                            }
                                            printWriter.println(String.format("%d,%s,\"%s\",%d,\"%s\",\"%s\",%s", Integer.valueOf(logEntry2.id), logEntry2.Method, logEntry2.url.replace("\"", "\"\""), Integer.valueOf(logEntry2.original_len), logEntry2.low_len.replace("\"", "\"\""), logEntry2.Unauthorized_len.replace("\"", "\"\""), obj));
                                        }
                                    }
                                    JOptionPane.showMessageDialog((Component) null, "导出成功！");
                                    printWriter.close();
                                } finally {
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog((Component) null, "导出失败: " + e.getMessage());
                            }
                        }
                    }
                });
                jButton3.addActionListener(new ActionListener() { // from class: burp.BurpExtender.1.7
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (jButton3.getText().equals("启动白名单")) {
                            jButton3.setText("关闭白名单");
                            BurpExtender.this.white_URL = jTextField.getText();
                            BurpExtender.this.white_switchs = 1;
                            jTextField.setEditable(false);
                            jTextField.setForeground(Color.GRAY);
                            return;
                        }
                        jButton3.setText("启动白名单");
                        BurpExtender.this.white_switchs = 0;
                        jTextField.setEditable(true);
                        jTextField.setForeground(Color.BLACK);
                    }
                });
                jPanel2.add(jLabel);
                jPanel2.add(jLabel2);
                jPanel2.add(jLabel3);
                jPanel2.add(jCheckBox);
                jPanel2.add(jCheckBox3);
                JPanel jPanel4 = new JPanel(new GridLayout(1, 2, 5, 5));
                jPanel4.add(jButton);
                jPanel4.add(jButton2);
                jPanel2.add(jPanel4);
                jPanel2.add(jLabel5);
                jPanel2.add(jTextField);
                jPanel2.add(jButton3);
                BurpExtender.this.tabs = new JTabbedPane();
                BurpExtender.this.requestViewer = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                BurpExtender.this.responseViewer = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                BurpExtender.this.requestViewer_1 = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                BurpExtender.this.responseViewer_1 = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                BurpExtender.this.requestViewer_2 = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                BurpExtender.this.responseViewer_2 = iBurpExtenderCallbacks.createMessageEditor(BurpExtender.this, false);
                final JSplitPane jSplitPane3 = new JSplitPane(1);
                jSplitPane3.setResizeWeight(0.5d);
                jSplitPane3.setLeftComponent(BurpExtender.this.requestViewer.getComponent());
                jSplitPane3.setRightComponent(BurpExtender.this.responseViewer.getComponent());
                final JSplitPane jSplitPane4 = new JSplitPane(1);
                jSplitPane4.setResizeWeight(0.5d);
                jSplitPane4.setLeftComponent(BurpExtender.this.requestViewer_1.getComponent());
                jSplitPane4.setRightComponent(BurpExtender.this.responseViewer_1.getComponent());
                final JSplitPane jSplitPane5 = new JSplitPane(1);
                jSplitPane5.setResizeWeight(0.5d);
                jSplitPane5.setLeftComponent(BurpExtender.this.requestViewer_2.getComponent());
                jSplitPane5.setRightComponent(BurpExtender.this.responseViewer_2.getComponent());
                BurpExtender.this.tabs.addTab("原始数据包", jSplitPane3);
                BurpExtender.this.tabs.addTab("低权限数据包", jSplitPane4);
                BurpExtender.this.tabs.addTab("未授权数据包", jSplitPane5);
                jSplitPane2.setLeftComponent(jPanel2);
                jSplitPane2.setRightComponent(jPanel3);
                jSplitPane.setLeftComponent(jPanel);
                jSplitPane.setRightComponent(BurpExtender.this.tabs);
                BurpExtender.this.splitPane.setLeftComponent(jSplitPane);
                BurpExtender.this.splitPane.setRightComponent(jSplitPane2);
                BurpExtender.this.splitPane.setResizeWeight(0.75d);
                BurpExtender.this.splitPane.addHierarchyListener(new HierarchyListener() { // from class: burp.BurpExtender.1.8
                    public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                        if ((hierarchyEvent.getChangeFlags() & 4) != 0 && BurpExtender.this.splitPane.isShowing()) {
                            BurpExtender.this.splitPane.setDividerLocation(0.75d);
                            jSplitPane3.setDividerLocation(0.5d);
                            jSplitPane4.setDividerLocation(0.5d);
                            jSplitPane5.setDividerLocation(0.5d);
                        }
                    }
                });
                iBurpExtenderCallbacks.customizeUiComponent(BurpExtender.this.splitPane);
                iBurpExtenderCallbacks.customizeUiComponent(BurpExtender.this.logTable);
                iBurpExtenderCallbacks.customizeUiComponent(jScrollPane);
                iBurpExtenderCallbacks.customizeUiComponent(jPanel2);
                iBurpExtenderCallbacks.customizeUiComponent(jPanel);
                iBurpExtenderCallbacks.customizeUiComponent(BurpExtender.this.tabs);
                iBurpExtenderCallbacks.addSuiteTab(BurpExtender.this);
                iBurpExtenderCallbacks.registerHttpListener(BurpExtender.this);
                iBurpExtenderCallbacks.registerScannerCheck(BurpExtender.this);
            }
        });
    }

    @Override // burp.ITab
    public String getTabCaption() {
        return "xia Yue";
    }

    @Override // burp.ITab
    public Component getUiComponent() {
        return this.splitPane;
    }

    @Override // burp.IBurpExtender, burp.IHttpListener
    public void processHttpMessage(final int i, boolean z, final IHttpRequestResponse iHttpRequestResponse) {
        if (this.switchs == 1 && i == 4 && !z) {
            synchronized (this.log) {
                new Thread(new Runnable() { // from class: burp.BurpExtender.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            BurpExtender.this.checkVul(iHttpRequestResponse, i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            BurpExtender.this.stdout.println(e);
                        }
                    }
                }).start();
            }
        }
    }

    @Override // burp.IScannerCheck
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse iHttpRequestResponse) {
        return null;
    }

    private void checkVul(IHttpRequestResponse iHttpRequestResponse, int i) {
        String MD5;
        String str;
        String str2;
        this.temp_data = String.valueOf(this.helpers.analyzeRequest(iHttpRequestResponse).getUrl());
        this.original_data_len = iHttpRequestResponse.getResponse().length;
        int bodyOffset = this.original_data_len - this.helpers.analyzeResponse(iHttpRequestResponse.getResponse()).getBodyOffset();
        String str3 = this.temp_data.split("\\?")[0];
        String[] split = this.white_URL.split(",");
        if (this.white_switchs == 1) {
            boolean z = false;
            for (String str4 : split) {
                if (str3.contains(str4)) {
                    this.stdout.println("白名单URL！" + str3);
                    z = true;
                }
            }
            if (!z) {
                this.stdout.println("不是白名单URL！" + str3);
                return;
            }
        }
        if (i == 4 || i == 64) {
            String[] split2 = str3.split("\\.");
            String str5 = split2[split2.length - 1];
            for (String str6 : new String[]{"jpg", "png", "gif", "css", "js", "pdf", "mp3", "mp4", "avi", "map", "svg", "ico", "svg", "woff", "woff2", "ttf"}) {
                if (str5.equals(str6)) {
                    this.stdout.println("当前url为静态文件：" + str3 + "\n");
                    return;
                }
            }
        }
        if (this.dedupe_mode == 1) {
            MD5 = MD5(iHttpRequestResponse.getRequest());
            this.stdout.println("\n全包MD5去重模式: " + MD5);
        } else {
            Iterator<IParameter> it = this.helpers.analyzeRequest(iHttpRequestResponse).getParameters().iterator();
            while (it.hasNext()) {
                str3 = str3 + "+" + it.next().getName();
            }
            MD5 = MD5(str3 + "+" + this.helpers.analyzeRequest(iHttpRequestResponse).getMethod());
            this.stdout.println("\n逻辑去重模式(URL+参数名): " + MD5);
        }
        this.stdout.println(MD5);
        Iterator<Request_md5> it2 = this.log4_md5.iterator();
        while (it2.hasNext()) {
            if (it2.next().md5_data.equals(MD5)) {
                return;
            }
        }
        this.log4_md5.add(new Request_md5(MD5));
        IRequestInfo analyzeRequest = this.helpers.analyzeRequest(iHttpRequestResponse);
        IHttpService httpService = iHttpRequestResponse.getHttpService();
        byte[] bytes = this.helpers.bytesToString(iHttpRequestResponse.getRequest()).substring(analyzeRequest.getBodyOffset()).getBytes();
        List<String> headers = analyzeRequest.getHeaders();
        String[] split3 = this.data_1.split("\n");
        int i2 = 0;
        while (i2 < headers.size()) {
            String str7 = headers.get(i2).split(":")[0];
            for (String str8 : split3) {
                if (str7.equals(str8.split(":")[0])) {
                    headers.remove(i2);
                    i2--;
                }
            }
            i2++;
        }
        for (String str9 : split3) {
            headers.add(headers.size() / 2, str9);
        }
        IHttpRequestResponse makeHttpRequest = this.callbacks.makeHttpRequest(httpService, this.helpers.buildHttpMessage(headers, bytes));
        int length = makeHttpRequest.getResponse().length - this.helpers.analyzeResponse(makeHttpRequest.getResponse()).getBodyOffset();
        if (bodyOffset == 0) {
            str = Integer.toString(length);
        } else if (bodyOffset == length) {
            str = Integer.toString(length) + "  ✔";
        } else {
            str = Integer.toString(length) + "  ==> " + Integer.toString(bodyOffset - length);
        }
        List<String> headers2 = analyzeRequest.getHeaders();
        String[] split4 = this.data_2.split("\n");
        int i3 = 0;
        while (i3 < headers2.size()) {
            String str10 = headers2.get(i3).split(":")[0];
            for (String str11 : split4) {
                if (str10.equals(str11)) {
                    headers2.remove(i3);
                    i3--;
                }
            }
            i3++;
        }
        if (this.universal_cookie.length() != 0) {
            String[] split5 = this.universal_cookie.split("\n");
            headers2.add(headers2.size() / 2, split5[0]);
            headers2.add(headers2.size() / 2, split5[1]);
        }
        IHttpRequestResponse makeHttpRequest2 = this.callbacks.makeHttpRequest(httpService, this.helpers.buildHttpMessage(headers2, bytes));
        int length2 = makeHttpRequest2.getResponse().length - this.helpers.analyzeResponse(makeHttpRequest2.getResponse()).getBodyOffset();
        if (bodyOffset == 0) {
            str2 = Integer.toString(length2);
        } else if (bodyOffset == length2) {
            str2 = Integer.toString(length2) + "  ✔";
        } else {
            str2 = Integer.toString(length2) + "  ==> " + Integer.toString(bodyOffset - length2);
        }
        this.conut++;
        this.log.add(new LogEntry(this.conut, this.helpers.analyzeRequest(iHttpRequestResponse).getMethod(), this.callbacks.saveBuffersToTempFiles(iHttpRequestResponse), this.callbacks.saveBuffersToTempFiles(makeHttpRequest), this.callbacks.saveBuffersToTempFiles(makeHttpRequest2), String.valueOf(this.helpers.analyzeRequest(iHttpRequestResponse).getUrl()), bodyOffset, str, str2));
        fireTableDataChanged();
        this.logTable.setRowSelectionInterval(this.select_row, this.select_row);
    }

    @Override // burp.IScannerCheck
    public List<IScanIssue> doActiveScan(IHttpRequestResponse iHttpRequestResponse, IScannerInsertionPoint iScannerInsertionPoint) {
        return null;
    }

    @Override // burp.IScannerCheck
    public int consolidateDuplicateIssues(IScanIssue iScanIssue, IScanIssue iScanIssue2) {
        if (iScanIssue.getIssueName().equals(iScanIssue2.getIssueName())) {
            return -1;
        }
        return 0;
    }

    public int getRowCount() {
        return this.log.size();
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return "选择";
            case 1:
                return "#";
            case 2:
                return "类型";
            case 3:
                return "URL";
            case 4:
                return "原始包长度";
            case 5:
                return "低权限包长度";
            case 6:
                return "未授权包长度";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int i) {
        if (i == 0) {
            return Boolean.class;
        }
        if (i == 1 || i == 4) {
            return Integer.class;
        }
        return String.class;
    }

    public boolean isCellEditable(int i, int i2) {
        return i2 == 0;
    }

    public void setValueAt(Object obj, int i, int i2) {
        if (i2 == 0) {
            this.log.get(i).selected = ((Boolean) obj).booleanValue();
            fireTableCellUpdated(i, i2);
            if (this.logTable != null) {
                this.logTable.getTableHeader().repaint();
            }
        }
    }

    public Object getValueAt(int i, int i2) {
        LogEntry logEntry = this.log.get(i);
        switch (i2) {
            case 0:
                return Boolean.valueOf(logEntry.selected);
            case 1:
                return Integer.valueOf(logEntry.id);
            case 2:
                return logEntry.Method;
            case 3:
                return logEntry.url;
            case 4:
                return Integer.valueOf(logEntry.original_len);
            case 5:
                return logEntry.low_len;
            case 6:
                return logEntry.Unauthorized_len;
            default:
                return "";
        }
    }

    @Override // burp.IHttpListener, burp.IMessageEditorController
    public byte[] getRequest() {
        return this.currentlyDisplayedItem.getRequest();
    }

    @Override // burp.IBurpExtender, burp.IMessageEditorController
    public byte[] getResponse() {
        return this.currentlyDisplayedItem.getResponse();
    }

    @Override // burp.IBurpExtender, burp.IMessageEditorController
    public IHttpService getHttpService() {
        return this.currentlyDisplayedItem.getHttpService();
    }

    /* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender$IndeterminateIcon.class */
    private static class IndeterminateIcon implements Icon {
        private final Icon icon = UIManager.getIcon("CheckBox.icon");

        private IndeterminateIcon() {
        }

        public void paintIcon(Component component, Graphics graphics, int i, int i2) {
            Icon icon = this.icon;
            if (icon != null) {
                icon.paintIcon(component, graphics, i, i2);
                int iconWidth = getIconWidth();
                int iconHeight = getIconHeight();
                graphics.setColor(Color.BLACK);
                graphics.fillRect(i + 3, (i2 + (iconHeight / 2)) - 1, iconWidth - 6, 2);
            }
        }

        public int getIconWidth() {
            Icon icon = this.icon;
            if (icon != null) {
                return icon.getIconWidth();
            }
            return 16;
        }

        public int getIconHeight() {
            Icon icon = this.icon;
            if (icon != null) {
                return icon.getIconHeight();
            }
            return 16;
        }
    }

    /* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender$HeaderRenderer.class */
    private class HeaderRenderer extends JCheckBox implements TableCellRenderer {
        private final IndeterminateIcon indeterminateIcon = new IndeterminateIcon();

        public HeaderRenderer() {
            setHorizontalAlignment(0);
        }

        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z, boolean z2, int i, int i2) {
            if (jTable != null) {
                setFont(jTable.getTableHeader().getFont());
                setForeground(jTable.getTableHeader().getForeground());
                setBackground(jTable.getTableHeader().getBackground());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            }
            int i3 = 0;
            synchronized (BurpExtender.this.log) {
                Iterator<LogEntry> it = BurpExtender.this.log.iterator();
                while (it.hasNext()) {
                    if (it.next().selected) {
                        i3++;
                    }
                }
                if (i3 > 0 && i3 < BurpExtender.this.log.size()) {
                    setSelected(false);
                    setIcon(this.indeterminateIcon);
                } else {
                    setIcon(null);
                    setSelected(i3 > 0 && i3 == BurpExtender.this.log.size());
                }
            }
            return this;
        }
    }

    /* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender$Table.class */
    private class Table extends JTable {
        public Table(TableModel tableModel) {
            super(tableModel);
        }

        public void changeSelection(int i, int i2, boolean z, boolean z2) {
            LogEntry logEntry = BurpExtender.this.log.get(BurpExtender.this.logTable.convertRowIndexToModel(i));
            BurpExtender.this.select_row = i;
            if (i2 == 5) {
                BurpExtender.this.tabs.setSelectedIndex(1);
            } else if (i2 == 6) {
                BurpExtender.this.tabs.setSelectedIndex(2);
            } else if (i2 >= 1 && i2 <= 4) {
                BurpExtender.this.tabs.setSelectedIndex(0);
            }
            BurpExtender.this.requestViewer.setMessage(logEntry.requestResponse.getRequest(), true);
            BurpExtender.this.responseViewer.setMessage(logEntry.requestResponse.getResponse(), false);
            BurpExtender.this.currentlyDisplayedItem = logEntry.requestResponse;
            BurpExtender.this.requestViewer_1.setMessage(logEntry.requestResponse_1.getRequest(), true);
            BurpExtender.this.responseViewer_1.setMessage(logEntry.requestResponse_1.getResponse(), false);
            BurpExtender.this.currentlyDisplayedItem_1 = logEntry.requestResponse_1;
            BurpExtender.this.requestViewer_2.setMessage(logEntry.requestResponse_2.getRequest(), true);
            BurpExtender.this.responseViewer_2.setMessage(logEntry.requestResponse_2.getResponse(), false);
            BurpExtender.this.currentlyDisplayedItem_2 = logEntry.requestResponse_2;
            super.changeSelection(i, i2, z, z2);
        }
    }

    /* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender$Request_md5.class */
    private static class Request_md5 {
        final String md5_data;

        Request_md5(String str) {
            this.md5_data = str;
        }
    }

    /* loaded from: xia_yue.1.5.jdk16.jar:burp/BurpExtender$LogEntry.class */
    private static class LogEntry {
        boolean selected = false;
        final int id;
        final String Method;
        final IHttpRequestResponsePersisted requestResponse;
        final IHttpRequestResponsePersisted requestResponse_1;
        final IHttpRequestResponsePersisted requestResponse_2;
        final String url;
        final int original_len;
        final String low_len;
        final String Unauthorized_len;

        LogEntry(int i, String str, IHttpRequestResponsePersisted iHttpRequestResponsePersisted, IHttpRequestResponsePersisted iHttpRequestResponsePersisted2, IHttpRequestResponsePersisted iHttpRequestResponsePersisted3, String str2, int i2, String str3, String str4) {
            this.id = i;
            this.Method = str;
            this.requestResponse = iHttpRequestResponsePersisted;
            this.requestResponse_1 = iHttpRequestResponsePersisted2;
            this.requestResponse_2 = iHttpRequestResponsePersisted3;
            this.url = str2;
            this.original_len = i2;
            this.low_len = str3;
            this.Unauthorized_len = str4;
        }
    }

    public static String MD5(String str) {
        return MD5(str.getBytes());
    }

    public static String MD5(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            byte[] digest = messageDigest.digest();
            char[] cArr2 = new char[digest.length * 2];
            int i = 0;
            for (byte b : digest) {
                int i2 = i + 1;
                cArr2[i] = cArr[(b >>> 4) & 15];
                i = i2 + 1;
                cArr2[i2] = cArr[b & 15];
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }
}
