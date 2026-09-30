package hospital;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Premium Swing GUI for the Intelligent Patient Priority Management System.
 * The GUI sits on top of the same HospitalManager DSA backend.
 */
public class Main {
    private final HospitalManager hospital = new HospitalManager();
    private final JFrame frame = new JFrame("IPPS | Intelligent Patient Priority Management System");
    private final JPanel content = new JPanel(new BorderLayout());
    private final JLabel pageTitle = new JLabel("Dashboard");
    private final JLabel pageSubtitle = new JLabel("Hospital operations at a glance");
    private final JLabel clock = new JLabel();
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private javax.swing.Timer timer;

    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color NAVY2 = new Color(30, 41, 59);
    private static final Color BG = new Color(245, 247, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(15, 23, 42);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color CYAN = new Color(8, 145, 178);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color RED = new Color(220, 38, 38);
    private static final Color AMBER = new Color(217, 119, 6);
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private void start() {
        seedDemoData();
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1180, 760));
        frame.setSize(1280, 820);
        frame.setLocationRelativeTo(null);
        buildShell();
        showDashboard();
        frame.setVisible(true);
        timer = new javax.swing.Timer(1000, e -> updateClock());
        timer.start();
        updateClock();
    }

    private void seedDemoData() {
        hospital.addPatient(new Patient(101, "Arun", 22, "9876543210"));
        hospital.addPatient(new Patient(102, "Priya", 30, "9876501234"));
        hospital.addPatient(new Patient(103, "Karthik", 45, "9876512345"));
        hospital.addDoctor(new Doctor(201, "Ravi", "Emergency"));
        hospital.addDoctor(new Doctor(202, "Meena", "Cardiology"));
        hospital.addDoctor(new Doctor(203, "Suresh", "General Medicine"));
    }

    private void buildShell() {
        frame.getContentPane().setBackground(BG);
        frame.setLayout(new BorderLayout());
        frame.add(buildSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG);
        right.add(buildTopbar(), BorderLayout.NORTH);
        content.setBackground(BG);
        right.add(content, BorderLayout.CENTER);
        frame.add(right, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(NAVY);
        side.setPreferredSize(new Dimension(250, 0));
        side.setBorder(new EmptyBorder(22, 16, 18, 16));

        JPanel brand = new JPanel(new BorderLayout(12, 0));
        brand.setOpaque(false);
        JLabel logo = new JLabel("✚");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setOpaque(true);
        logo.setBackground(BLUE);
        logo.setPreferredSize(new Dimension(48, 48));
        brand.add(logo, BorderLayout.WEST);
        JPanel bt = new JPanel(); bt.setOpaque(false); bt.setLayout(new BoxLayout(bt, BoxLayout.Y_AXIS));
        JLabel b1 = label("IPPS", Color.WHITE, 20, true);
        JLabel b2 = label("Hospital Intelligence", new Color(148, 163, 184), 11, false);
        bt.add(b1); bt.add(Box.createVerticalStrut(2)); bt.add(b2);
        brand.add(bt, BorderLayout.CENTER);
        side.add(brand, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setBorder(new EmptyBorder(28, 0, 0, 0));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        addNav(menu, "Dashboard", "⌂", this::showDashboard);
        addNav(menu, "Patients", "●", this::showPatients);
        addNav(menu, "Emergency Queue", "!", this::showEmergency);
        addNav(menu, "Appointments", "□", this::showAppointments);
        addNav(menu, "Doctors", "+", this::showDoctors);
        addNav(menu, "Medical History", "▤", this::showHistory);
        addNav(menu, "Statistics", "▥", this::showStatistics);
        menu.add(Box.createVerticalGlue());
        addNav(menu, "Undo Last Action", "↶", () -> toast(hospital.undoLastAction(), "Action History"));
        addNav(menu, "About Project", "i", this::showAbout);
        side.add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(); footer.setOpaque(false); footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        JSeparator sep = new JSeparator(); sep.setForeground(new Color(51, 65, 85)); footer.add(sep);
        footer.add(Box.createVerticalStrut(12));
        JLabel course = label("ADVANCED DATA STRUCTURES", new Color(148,163,184), 10, true);
        JLabel java = label("with Java • DSA-powered", new Color(100,116,139), 10, false);
        footer.add(course); footer.add(java);
        side.add(footer, BorderLayout.SOUTH);
        return side;
    }

    private void addNav(JPanel menu, String text, String icon, Runnable action) {
        JButton b = new JButton();
        b.setLayout(new BorderLayout(12,0));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setPreferredSize(new Dimension(218,46));
        b.setFocusPainted(false); b.setBorder(new EmptyBorder(0, 12, 0, 10));
        b.setContentAreaFilled(false); b.setOpaque(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel ic = label(icon, new Color(148,163,184), 18, true); ic.setHorizontalAlignment(SwingConstants.CENTER); ic.setPreferredSize(new Dimension(28,30));
        JLabel tx = label(text, new Color(203,213,225), 13, true);
        b.add(ic, BorderLayout.WEST); b.add(tx, BorderLayout.CENTER);
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ b.setOpaque(true); b.setBackground(NAVY2); }
            public void mouseExited(MouseEvent e){ b.setOpaque(false); b.repaint(); }
        });
        b.addActionListener(e -> action.run());
        navButtons.put(text,b);
        menu.add(b); menu.add(Box.createVerticalStrut(5));
    }

    private JPanel buildTopbar() {
        JPanel top = new JPanel(new BorderLayout()); top.setBackground(Color.WHITE); top.setBorder(new MatteBorder(0,0,1,0,new Color(226,232,240)));
        JPanel left = new JPanel(); left.setOpaque(false); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS)); left.setBorder(new EmptyBorder(18,28,16,10));
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 25)); pageTitle.setForeground(TEXT);
        pageSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12)); pageSubtitle.setForeground(MUTED);
        left.add(pageTitle); left.add(Box.createVerticalStrut(4)); left.add(pageSubtitle);
        top.add(left, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 18)); right.setOpaque(false);
        clock.setForeground(MUTED); clock.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel status = pill("● SYSTEM ONLINE", new Color(220,252,231), GREEN);
        right.add(status); right.add(clock); top.add(right, BorderLayout.EAST);
        return top;
    }

    private void updateClock() { clock.setText(new java.text.SimpleDateFormat("EEE, dd MMM yyyy  •  hh:mm a").format(new Date())); }

    private void setPage(String title, String subtitle) { pageTitle.setText(title); pageSubtitle.setText(subtitle); content.removeAll(); }

    private void finishPage() { content.revalidate(); content.repaint(); }

    private void showDashboard() {
        setPage("Dashboard", "Hospital operations at a glance");
        JPanel root = new JPanel(new BorderLayout(0,18)); root.setOpaque(false); root.setBorder(new EmptyBorder(22,28,24,28));
        JPanel cards = new JPanel(new GridLayout(1,4,14,0)); cards.setOpaque(false);
        cards.add(statCard("TOTAL PATIENTS", String.valueOf(hospital.totalPatients()), "Registered patients", BLUE, "●"));
        cards.add(statCard("DOCTORS", String.valueOf(hospital.totalDoctors()), "Available doctors", CYAN, "+"));
        cards.add(statCard("EMERGENCY WAITING", String.valueOf(hospital.emergencyCount()), "Priority queue", RED, "!"));
        cards.add(statCard("APPOINTMENTS", String.valueOf(hospital.appointmentCount()), "Waiting in queue", AMBER, "□"));
        root.add(cards, BorderLayout.NORTH);

        JPanel middle = new JPanel(new GridLayout(1,2,18,0)); middle.setOpaque(false);
        middle.add(buildQuickActions());
        middle.add(buildPriorityPanel());
        root.add(middle, BorderLayout.CENTER);
        JPanel info = cardPanel(); info.setLayout(new BorderLayout(12,0));
        info.add(label("DSA CORE", BLUE, 11, true), BorderLayout.WEST);
        info.add(label("ArrayList  •  HashMap  •  Queue  •  PriorityQueue  •  Stack  •  Comparable", MUTED, 12, false), BorderLayout.CENTER);
        root.add(info, BorderLayout.SOUTH);
        content.add(root, BorderLayout.CENTER); finishPage();
    }

    private JPanel statCard(String title, String value, String sub, Color accent, String icon) {
        JPanel p = cardPanel(); p.setBorder(new CompoundBorder(new LineBorder(new Color(226,232,240),1,true), new EmptyBorder(16,18,16,18)));
        p.setLayout(new BorderLayout(12,0));
        JLabel ic = label(icon, Color.WHITE, 20, true); ic.setHorizontalAlignment(SwingConstants.CENTER); ic.setOpaque(true); ic.setBackground(accent); ic.setPreferredSize(new Dimension(44,44));
        p.add(ic, BorderLayout.WEST);
        JPanel t = new JPanel(); t.setOpaque(false); t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(label(title, MUTED, 10, true)); t.add(Box.createVerticalStrut(4)); t.add(label(value, TEXT, 26, true)); t.add(Box.createVerticalStrut(2)); t.add(label(sub, MUTED, 11, false));
        p.add(t, BorderLayout.CENTER); return p;
    }

    private JPanel buildQuickActions() {
        JPanel p = cardPanel(); p.setLayout(new BorderLayout());
        p.add(sectionHeader("Quick Actions", "Common hospital operations"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3,2,12,12)); grid.setOpaque(false); grid.setBorder(new EmptyBorder(4,18,18,18));
        actionButton(grid,"Register Patient","Add a new patient",BLUE,this::registerPatient);
        actionButton(grid,"Emergency Intake","Add to priority queue",RED,this::addEmergency);
        actionButton(grid,"Book Appointment","Schedule consultation",AMBER,this::bookAppointment);
        actionButton(grid,"Add Doctor","Create doctor profile",CYAN,this::addDoctor);
        actionButton(grid,"Search Patient","Find by patient ID",GREEN,this::searchPatient);
        actionButton(grid,"Treat Next","Serve highest priority",new Color(124,58,237),this::treatEmergency);
        p.add(grid,BorderLayout.CENTER); return p;
    }

    private void actionButton(JPanel grid,String title,String sub,Color accent,Runnable r){
        JButton b=new JButton(); b.setLayout(new BorderLayout(10,0)); b.setFocusPainted(false); b.setBackground(Color.WHITE); b.setBorder(new CompoundBorder(new LineBorder(new Color(226,232,240),1,true),new EmptyBorder(10,12,10,10))); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel mark=label("•",accent,22,true); mark.setHorizontalAlignment(SwingConstants.CENTER); mark.setPreferredSize(new Dimension(22,28)); b.add(mark,BorderLayout.WEST);
        JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));x.add(label(title,TEXT,12,true));x.add(Box.createVerticalStrut(3));x.add(label(sub,MUTED,10,false));b.add(x,BorderLayout.CENTER);b.addActionListener(e->r.run());grid.add(b);
    }

    private JPanel buildPriorityPanel(){
        JPanel p=cardPanel();p.setLayout(new BorderLayout());p.add(sectionHeader("Emergency Priority Queue","Critical cases are served first"),BorderLayout.NORTH);
        JPanel list=new JPanel();list.setOpaque(false);list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));list.setBorder(new EmptyBorder(2,18,16,18));
        if(hospital.getEmergencyQueue().isEmpty()) list.add(emptyState("No emergency patients waiting","Priority queue is currently clear."));
        else { ArrayList<EmergencyPatient> es=new ArrayList<>(hospital.getEmergencyQueue()); Collections.sort(es); int i=1; for(EmergencyPatient e:es){list.add(priorityRow(i++,e));list.add(Box.createVerticalStrut(8));} }
        JScrollPane sp=new JScrollPane(list);sp.setBorder(null);sp.getViewport().setBackground(Color.WHITE);p.add(sp,BorderLayout.CENTER);return p;
    }

    private JPanel priorityRow(int n, EmergencyPatient e){
        JPanel r=new JPanel(new BorderLayout(10,0));r.setBackground(new Color(248,250,252));r.setBorder(new CompoundBorder(new LineBorder(new Color(226,232,240),1,true),new EmptyBorder(9,10,9,10)));r.setMaximumSize(new Dimension(Integer.MAX_VALUE,58));
        JLabel no=label(String.valueOf(n),MUTED,12,true);no.setHorizontalAlignment(SwingConstants.CENTER);no.setPreferredSize(new Dimension(24,30));r.add(no,BorderLayout.WEST);
        JPanel mid=new JPanel();mid.setOpaque(false);mid.setLayout(new BoxLayout(mid,BoxLayout.Y_AXIS));mid.add(label(e.getPatient().getName(),TEXT,12,true));mid.add(Box.createVerticalStrut(2));mid.add(label("Patient ID: "+e.getPatient().getId(),MUTED,10,false));r.add(mid,BorderLayout.CENTER);
        Color c=e.getSeverity()==1?RED:e.getSeverity()==2?AMBER:GREEN;r.add(pill(e.severityText(),new Color(c.getRed(),c.getGreen(),c.getBlue(),25),c),BorderLayout.EAST);return r;
    }

    private void showPatients(){
        setPage("Patient Management","Register, search and review patients");
        JPanel root=pagePanel();
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));actions.setOpaque(false);actions.add(primaryButton("+ Register Patient",this::registerPatient));actions.add(secondaryButton("Search by ID",this::searchPatient));
        root.add(actions,BorderLayout.NORTH);root.add(tablePanel("Registered Patients",patientTable()),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }

    private JTable patientTable(){DefaultTableModel m=new DefaultTableModel(new Object[]{"PATIENT ID","PATIENT","AGE","PHONE","STATUS"},0){public boolean isCellEditable(int r,int c){return false;}};for(Patient p:hospital.getPatients())m.addRow(new Object[]{p.getId(),p.getName(),p.getAge(),p.getPhone(),"Active"});JTable t=styledTable(m);t.getColumnModel().getColumn(4).setCellRenderer(new PillRenderer(GREEN));return t;}

    private void showEmergency(){
        setPage("Emergency Command Center","PriorityQueue controls critical patient flow");
        JPanel root=pagePanel();
        JPanel top=new JPanel(new BorderLayout(12,0));top.setOpaque(false);top.add(emergencySummary(),BorderLayout.CENTER);JPanel a=new JPanel(new FlowLayout(FlowLayout.RIGHT));a.setOpaque(false);a.add(primaryButton("+ Emergency Intake",this::addEmergency));a.add(dangerButton("Treat Next Patient",this::treatEmergency));top.add(a,BorderLayout.EAST);root.add(top,BorderLayout.NORTH);
        DefaultTableModel m=new DefaultTableModel(new Object[]{"QUEUE","PATIENT ID","PATIENT","SEVERITY","PROCESSING ORDER"},0){public boolean isCellEditable(int r,int c){return false;}};ArrayList<EmergencyPatient> es=new ArrayList<>(hospital.getEmergencyQueue());Collections.sort(es);int i=1;for(EmergencyPatient e:es)m.addRow(new Object[]{i++,e.getPatient().getId(),e.getPatient().getName(),e.severityText(),e.getSeverity()==1?"Immediate":e.getSeverity()==2?"High":"Standard"});JTable t=styledTable(m);t.getColumnModel().getColumn(3).setCellRenderer(new SeverityRenderer());root.add(tablePanel("Current Priority Queue",t),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }

    private JPanel emergencySummary(){JPanel p=cardPanel();p.setLayout(new BorderLayout(12,0));JLabel ic=label("!",Color.WHITE,22,true);ic.setOpaque(true);ic.setBackground(RED);ic.setHorizontalAlignment(SwingConstants.CENTER);ic.setPreferredSize(new Dimension(48,48));p.add(ic,BorderLayout.WEST);JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));x.add(label("ACTIVE EMERGENCY CASES",MUTED,10,true));x.add(Box.createVerticalStrut(3));x.add(label(String.valueOf(hospital.emergencyCount()),TEXT,25,true));x.add(label("Priority 1 → Critical  |  2 → Serious  |  3 → Normal",MUTED,10,false));p.add(x,BorderLayout.CENTER);return p;}

    private void showAppointments(){
        setPage("Appointment Desk","FIFO queue for scheduled consultations");
        JPanel root=pagePanel();JPanel a=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));a.setOpaque(false);a.add(primaryButton("+ Book Appointment",this::bookAppointment));a.add(secondaryButton("Serve Next",this::serveAppointment));root.add(a,BorderLayout.NORTH);
        DefaultTableModel m=new DefaultTableModel(new Object[]{"QUEUE","PATIENT","DOCTOR","SPECIALIZATION","DATE / TIME"},0){public boolean isCellEditable(int r,int c){return false;}};int i=1;for(Appointment x:hospital.getAppointments())m.addRow(new Object[]{i++,x.getPatient().getName(),"Dr. "+x.getDoctor().getName(),x.getDoctor().getDepartment(),x.toString()});root.add(tablePanel("Waiting Appointment Queue",styledTable(m)),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }

    private void showDoctors(){
        setPage("Doctor Directory","Available specialists and departments");JPanel root=pagePanel();JPanel a=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));a.setOpaque(false);a.add(primaryButton("+ Add Doctor",this::addDoctor));root.add(a,BorderLayout.NORTH);
        DefaultTableModel m=new DefaultTableModel(new Object[]{"DOCTOR ID","DOCTOR","SPECIALIZATION","STATUS"},0){public boolean isCellEditable(int r,int c){return false;}};for(Doctor d:hospital.getDoctors())m.addRow(new Object[]{d.getId(),"Dr. "+d.getName(),d.getDepartment(),"Available"});JTable t=styledTable(m);t.getColumnModel().getColumn(3).setCellRenderer(new PillRenderer(GREEN));root.add(tablePanel("Medical Staff",t),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }

    private void showHistory(){
        setPage("Medical History","Patient treatment and appointment timeline");JPanel root=pagePanel();JPanel a=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));a.setOpaque(false);a.add(primaryButton("View Patient History",this::medicalHistory));root.add(a,BorderLayout.NORTH);
        DefaultTableModel m=new DefaultTableModel(new Object[]{"PATIENT ID","PATIENT","RECORDS","LATEST ACTIVITY"},0){public boolean isCellEditable(int r,int c){return false;}};for(Patient p:hospital.getPatients()){java.util.List<String> h=p.getHistory();m.addRow(new Object[]{p.getId(),p.getName(),h.size(),h.isEmpty()?"No records yet":h.get(h.size()-1)});}root.add(tablePanel("Patient Records",styledTable(m)),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }

    private void showStatistics(){
        setPage("Hospital Statistics","Operational snapshot and data-structure usage");JPanel root=pagePanel();
        JPanel cards=new JPanel(new GridLayout(1,4,14,0));cards.setOpaque(false);cards.add(statCard("PATIENTS",String.valueOf(hospital.totalPatients()),"ArrayList",BLUE,"●"));cards.add(statCard("SEARCH INDEX",String.valueOf(hospital.totalPatients()),"HashMap",GREEN,"#"));cards.add(statCard("EMERGENCY",String.valueOf(hospital.emergencyCount()),"PriorityQueue",RED,"!"));cards.add(statCard("APPOINTMENTS",String.valueOf(hospital.appointmentCount()),"Queue / FIFO",AMBER,"□"));root.add(cards,BorderLayout.NORTH);
        JPanel lower=new JPanel(new GridLayout(1,2,18,0));lower.setOpaque(false);lower.add(dsaExplanation());lower.add(analyticsPanel());root.add(lower,BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }


    private JPanel analyticsPanel(){
        JPanel p=cardPanel(); p.setLayout(new BorderLayout());
        p.add(sectionHeader("System Analytics","Current workload across core queues"),BorderLayout.NORTH);
        p.add(new WorkloadChart(hospital),BorderLayout.CENTER);
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.LEFT,10,8)); footer.setOpaque(false);
        footer.add(pill("PATIENTS  "+hospital.totalPatients(),new Color(219,234,254),BLUE));
        footer.add(pill("EMERGENCY  "+hospital.emergencyCount(),new Color(254,226,226),RED));
        footer.add(pill("APPOINTMENTS  "+hospital.appointmentCount(),new Color(254,243,199),AMBER));
        p.add(footer,BorderLayout.SOUTH); return p;
    }

    private static class WorkloadChart extends JPanel{
        private final HospitalManager h;
        WorkloadChart(HospitalManager h){this.h=h;setOpaque(false);setPreferredSize(new Dimension(420,250));}
        protected void paintComponent(Graphics g){super.paintComponent(g); Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(), base=getHeight()-48, left=34, top=24, chartW=w-58, chartH=base-top;
            int[] vals={h.totalPatients(),h.totalDoctors(),h.emergencyCount(),h.appointmentCount()};
            String[] names={"Patients","Doctors","Emergency","Appointments"};
            Color[] cs={BLUE,CYAN,RED,AMBER}; int max=1; for(int v:vals)max=Math.max(max,v);
            g2.setColor(new Color(226,232,240)); g2.drawLine(left,base,w-18,base);
            int slot=chartW/vals.length;
            for(int i=0;i<vals.length;i++){int bh=(int)((chartH-20)*(vals[i]/(double)max)); int bw=Math.max(34,slot-28); int x=left+i*slot+(slot-bw)/2; int y=base-bh;
                g2.setColor(cs[i]); g2.fillRoundRect(x,y,bw,bh,12,12);
                g2.setFont(new Font("SansSerif",Font.BOLD,12)); g2.setColor(TEXT); String v=String.valueOf(vals[i]); g2.drawString(v,x+bw/2-g2.getFontMetrics().stringWidth(v)/2,y-7);
                g2.setFont(new Font("SansSerif",Font.PLAIN,10)); g2.setColor(MUTED); String n=names[i]; g2.drawString(n,x+bw/2-g2.getFontMetrics().stringWidth(n)/2,base+20);
            } g2.dispose();}
    }

    private JPanel dsaExplanation(){JPanel p=cardPanel();p.setLayout(new BorderLayout());p.add(sectionHeader("Data Structures Used","Why each structure is appropriate"),BorderLayout.NORTH);String[][] rows={{"ArrayList","Patients & doctors","Dynamic collection"},{"HashMap","Patient lookup","Fast average lookup"},{"PriorityQueue","Emergency cases","Highest priority first"},{"Queue","Appointments","FIFO order"},{"Stack","Recent actions","LIFO history"},{"Comparable","Emergency ordering","Defines priority"}};JPanel list=new JPanel();list.setOpaque(false);list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));list.setBorder(new EmptyBorder(0,18,18,18));for(String[] r:rows){JPanel row=new JPanel(new GridLayout(1,3));row.setOpaque(false);row.setBorder(new EmptyBorder(8,0,8,0));row.add(label(r[0],BLUE,11,true));row.add(label(r[1],TEXT,11,true));row.add(label(r[2],MUTED,10,false));list.add(row);list.add(new JSeparator());}p.add(list,BorderLayout.CENTER);return p;}
    private JPanel flowExplanation(){JPanel p=cardPanel();p.setLayout(new BorderLayout());p.add(sectionHeader("Patient Priority Flow","How emergency treatment works"),BorderLayout.NORTH);JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));x.setBorder(new EmptyBorder(8,22,18,22));String[] steps={"1  Patient registered","2  Emergency severity selected","3  PriorityQueue stores case","4  Comparable defines severity ordering","5  Treat Next removes highest-priority case"};for(String s:steps){JLabel l=label(s,TEXT,12,s.startsWith("4")?false:true);l.setBorder(new EmptyBorder(8,0,8,0));x.add(l);}p.add(x,BorderLayout.CENTER);return p;}

    private void showAbout(){
        setPage("About the Project","Academic project overview");JPanel root=pagePanel();JPanel hero=cardPanel();hero.setLayout(new BorderLayout(20,0));hero.setBorder(new CompoundBorder(new LineBorder(new Color(191,219,254),1,true),new EmptyBorder(25,25,25,25)));JLabel logo=label("✚",Color.WHITE,36,true);logo.setOpaque(true);logo.setBackground(BLUE);logo.setHorizontalAlignment(SwingConstants.CENTER);logo.setPreferredSize(new Dimension(75,75));hero.add(logo,BorderLayout.WEST);JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));x.add(label("Intelligent Patient Priority Management System",TEXT,21,true));x.add(Box.createVerticalStrut(8));x.add(label("Java Swing GUI • Advanced Data Structures with Java",BLUE,13,true));x.add(Box.createVerticalStrut(10));x.add(label("A hospital workflow application demonstrating practical use of PriorityQueue, Queue, HashMap, ArrayList, Stack and Comparable.",MUTED,12,false));hero.add(x,BorderLayout.CENTER);root.add(hero,BorderLayout.NORTH);root.add(buildAboutCards(),BorderLayout.CENTER);content.add(root,BorderLayout.CENTER);finishPage();
    }
    private JPanel buildAboutCards(){JPanel p=new JPanel(new GridLayout(1,3,16,0));p.setOpaque(false);p.add(infoCard("PURPOSE","Organize patient care workflows and prioritize emergency cases efficiently.",BLUE));p.add(infoCard("CORE ALGORITHM","Emergency ordering is driven by Comparable inside a PriorityQueue.",RED));p.add(infoCard("UI LAYER","Swing dashboard provides tables, forms, queues and live statistics.",CYAN));return p;}
    private JPanel infoCard(String t,String s,Color c){JPanel p=cardPanel();p.setLayout(new BorderLayout(0,10));p.add(label(t,c,11,true),BorderLayout.NORTH);p.add(label("<html><div style='width:220px'>"+s+"</div></html>",TEXT,13,false),BorderLayout.CENTER);return p;}

    private JPanel pagePanel(){JPanel p=new JPanel(new BorderLayout(0,16));p.setOpaque(false);p.setBorder(new EmptyBorder(22,28,24,28));return p;}
    private JPanel tablePanel(String title,JTable table){JPanel p=cardPanel();p.setLayout(new BorderLayout());p.add(sectionHeader(title,"Live data from the in-memory hospital manager"),BorderLayout.NORTH);JScrollPane sp=new JScrollPane(table);sp.setBorder(null);sp.getViewport().setBackground(Color.WHITE);p.add(sp,BorderLayout.CENTER);return p;}
    private JTable styledTable(DefaultTableModel m){JTable t=new JTable(m);t.setRowHeight(44);t.setFont(FONT);t.setForeground(TEXT);t.setGridColor(new Color(241,245,249));t.setShowVerticalLines(false);t.setIntercellSpacing(new Dimension(0,1));t.setSelectionBackground(new Color(239,246,255));t.setSelectionForeground(TEXT);JTableHeader h=t.getTableHeader();h.setFont(new Font("SansSerif",Font.BOLD,10));h.setForeground(MUTED);h.setBackground(new Color(248,250,252));h.setPreferredSize(new Dimension(0,40));h.setBorder(new MatteBorder(0,0,1,0,new Color(226,232,240)));t.setAutoCreateRowSorter(true);return t;}

    private JPanel sectionHeader(String title,String sub){JPanel p=new JPanel(new BorderLayout());p.setOpaque(false);p.setBorder(new EmptyBorder(18,18,12,18));JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));x.add(label(title,TEXT,15,true));x.add(Box.createVerticalStrut(3));x.add(label(sub,MUTED,10,false));p.add(x,BorderLayout.WEST);return p;}
    private JPanel cardPanel(){JPanel p=new JPanel();p.setBackground(CARD);p.setBorder(new CompoundBorder(new LineBorder(new Color(226,232,240),1,true),new EmptyBorder(0,0,0,0)));return p;}
    private JLabel label(String text,Color color,int size,boolean bold){JLabel l=new JLabel(text);l.setForeground(color);l.setFont(new Font("SansSerif",bold?Font.BOLD:Font.PLAIN,size));return l;}
    private JLabel pill(String text,Color bg,Color fg){JLabel l=label(text,fg,10,true);l.setOpaque(true);l.setBackground(bg);l.setBorder(new EmptyBorder(6,10,6,10));return l;}
    private JPanel emptyState(String title,String sub){JPanel p=new JPanel();p.setOpaque(false);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.add(label(title,MUTED,13,true));p.add(label(sub,new Color(148,163,184),11,false));return p;}

    private JButton primaryButton(String text,Runnable r){return styledButton(text,BLUE,Color.WHITE,r);}
    private JButton dangerButton(String text,Runnable r){return styledButton(text,RED,Color.WHITE,r);}
    private JButton secondaryButton(String text,Runnable r){JButton b=styledButton(text,Color.WHITE,TEXT,r);b.setBorder(new CompoundBorder(new LineBorder(new Color(203,213,225),1,true),new EmptyBorder(9,14,9,14)));return b;}
    private JButton styledButton(String text,Color bg,Color fg,Runnable r){JButton b=new JButton(text);b.setFont(new Font("SansSerif",Font.BOLD,12));b.setForeground(fg);b.setBackground(bg);b.setFocusPainted(false);b.setBorder(new EmptyBorder(10,15,10,15));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.addActionListener(e->r.run());return b;}

    private void registerPatient(){
        JTextField id=new JTextField(),name=new JTextField(),age=new JTextField(),phone=new JTextField();
        if(!formDialog("Register New Patient",new String[]{"Patient ID","Full Name","Age","Phone"},new JTextField[]{id,name,age,phone}))return;
        try{boolean ok=hospital.addPatient(new Patient(Integer.parseInt(id.getText().trim()),name.getText().trim(),Integer.parseInt(age.getText().trim()),phone.getText().trim()));toast(ok?"Patient registered successfully.":"Patient ID already exists.",ok?"Patient Registered":"Duplicate ID");showPatients();}catch(Exception e){error("Please enter valid patient details.");}
    }
    private void addDoctor(){JTextField id=new JTextField(),name=new JTextField(),spec=new JTextField();if(!formDialog("Add Doctor",new String[]{"Doctor ID","Doctor Name","Specialization"},new JTextField[]{id,name,spec}))return;try{boolean ok=hospital.addDoctor(new Doctor(Integer.parseInt(id.getText().trim()),name.getText().trim(),spec.getText().trim()));toast(ok?"Doctor added successfully.":"Doctor ID already exists.","Doctor Directory");showDoctors();}catch(Exception e){error("Please enter valid doctor details.");}}
    private void addEmergency(){if(hospital.totalPatients()==0){error("Register a patient first.");return;}String[] ids=new String[hospital.getPatients().size()];for(int i=0;i<ids.length;i++){Patient p=hospital.getPatients().get(i);ids[i]=p.getId()+" — "+p.getName();}JComboBox<String> patient=new JComboBox<>(ids);JComboBox<String> sev=new JComboBox<>(new String[]{"1 — CRITICAL","2 — SERIOUS","3 — NORMAL"});if(!twoFieldDialog("Emergency Intake",new String[]{"Patient","Severity"},new JComponent[]{patient,sev}))return;int id=Integer.parseInt(((String)patient.getSelectedItem()).split(" ")[0]);Patient p=hospital.searchPatient(id);int severity=sev.getSelectedIndex()+1;hospital.addEmergency(p,severity);toast(p.getName()+" added to the emergency PriorityQueue.","Emergency Intake");showEmergency();}
    private void treatEmergency(){EmergencyPatient e=hospital.treatNextEmergency();if(e==null){toast("No emergency patients are waiting.","Emergency Queue");}else{toast("Treating: "+e.getPatient().getName()+" • "+e.severityText(),"Priority Patient Served");}showEmergency();}
    private void searchPatient(){String s=JOptionPane.showInputDialog(frame,"Enter Patient ID:");if(s==null)return;try{Patient p=hospital.searchPatient(Integer.parseInt(s.trim()));if(p==null)error("Patient not found.");else{String h=p.getHistory().isEmpty()?"No medical history yet.":String.join("\n",p.getHistory());toast("ID: "+p.getId()+"\nName: "+p.getName()+"\nAge: "+p.getAge()+"\nPhone: "+p.getPhone()+"\n\nHistory:\n"+h,"Patient Profile");}}catch(Exception e){error("Invalid patient ID.");}}
    private void bookAppointment(){if(hospital.getDoctors().isEmpty()){error("Add a doctor first.");return;}String[] pids=new String[hospital.getPatients().size()];for(int i=0;i<pids.length;i++){Patient p=hospital.getPatients().get(i);pids[i]=p.getId()+" — "+p.getName();}String[] dids=new String[hospital.getDoctors().size()];for(int i=0;i<dids.length;i++){Doctor d=hospital.getDoctors().get(i);dids[i]=d.getId()+" — Dr. "+d.getName();}JComboBox<String> pc=new JComboBox<>(pids),dc=new JComboBox<>(dids);JTextField date=new JTextField("2026-09-20 10:00");if(!threeFieldDialog("Book Appointment",new String[]{"Patient","Doctor","Date / Time"},new JComponent[]{pc,dc,date}))return;int pid=Integer.parseInt(((String)pc.getSelectedItem()).split(" ")[0]);int did=Integer.parseInt(((String)dc.getSelectedItem()).split(" ")[0]);Patient p=hospital.searchPatient(pid);Doctor d=null;for(Doctor x:hospital.getDoctors())if(x.getId()==did)d=x;hospital.addAppointment(new Appointment(p,d,date.getText().trim()));toast("Appointment added to FIFO queue.","Appointment Booked");showAppointments();}
    private void serveAppointment(){Appointment a=hospital.serveNextAppointment();if(a==null)toast("No appointments are waiting.","Appointment Desk");else toast("Serving: "+a.getPatient().getName()+" with Dr. "+a.getDoctor().getName(),"Appointment Served");showAppointments();}
    private void medicalHistory(){String s=JOptionPane.showInputDialog(frame,"Enter Patient ID:");if(s==null)return;try{Patient p=hospital.searchPatient(Integer.parseInt(s.trim()));if(p==null){error("Patient not found.");return;}java.util.List<String> h=p.getHistory();toast(h.isEmpty()?"No medical history yet.":String.join("\n",h),"Medical History • "+p.getName());}catch(Exception e){error("Invalid patient ID.");}}

    private boolean formDialog(String title,String[] labels,JTextField[] fields){JPanel p=new JPanel(new GridLayout(labels.length,2,10,12));p.setBorder(new EmptyBorder(10,5,5,5));for(int i=0;i<labels.length;i++){p.add(label(labels[i],TEXT,12,true));p.add(fields[i]);}return JOptionPane.showConfirmDialog(frame,p,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION;}
    private boolean twoFieldDialog(String title,String[] labels,JComponent[] fields){return genericDialog(title,labels,fields);}
    private boolean threeFieldDialog(String title,String[] labels,JComponent[] fields){return genericDialog(title,labels,fields);}
    private boolean genericDialog(String title,String[] labels,JComponent[] fields){JPanel p=new JPanel(new GridLayout(labels.length,2,10,12));p.setBorder(new EmptyBorder(10,5,5,5));for(int i=0;i<labels.length;i++){p.add(label(labels[i],TEXT,12,true));p.add(fields[i]);}return JOptionPane.showConfirmDialog(frame,p,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION;}
    private void toast(String msg,String title){JOptionPane.showMessageDialog(frame,msg,title,JOptionPane.INFORMATION_MESSAGE);}
    private void error(String msg){JOptionPane.showMessageDialog(frame,msg,"Input / Operation",JOptionPane.WARNING_MESSAGE);}

    private static class PillRenderer extends DefaultTableCellRenderer{private final Color c;PillRenderer(Color c){this.c=c;}public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,col);l.setHorizontalAlignment(SwingConstants.CENTER);l.setForeground(c);l.setFont(new Font("SansSerif",Font.BOLD,10));return l;}}
    private static class SeverityRenderer extends DefaultTableCellRenderer{public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c);l.setHorizontalAlignment(SwingConstants.CENTER);l.setFont(new Font("SansSerif",Font.BOLD,10));String x=String.valueOf(v);l.setForeground(x.startsWith("CRITICAL")?RED:x.startsWith("SERIOUS")?AMBER:GREEN);return l;}}
}
