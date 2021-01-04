package pageDesign;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class calPage {
    public void getValue(JButton button,JLabel info){
        button.addActionListener(
                new ActionListener() {
                    String infoAll = "";
                    String tmp = "";
                    int last;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tmp = button.getText();
                        view.add(tmp);
                        lastScr.setText("上次的得数"+lastAns);
                        infoAll = traverse(view);
                        info.setText(infoAll);
                        if(tmp == "CE"){
                            empty();
                            infoAll = "";
                            info.setText(infoAll);
                        }
                        else if(tmp == "Del"){
                            view.remove(tmp);
                            last = view.size();
                            view.remove(last-1);
                            infoAll = traverse(view);
                            info.setText(infoAll);
                        }
                        else if(tmp == "="){
                            tidyNum();
                            infixScreen.setText("中缀 :"+traverse(view));
                            inTranSuffix(tidy);
                            suffixScreen.setText("后缀："+traverse(numList));
                            suffixCal();
                            for(String data:numList){
                                System.out.println(data);
                            }

                            info.setText("得数："+String.valueOf(suffixStack.peek()));
                            lastAns = String.valueOf(suffixStack.peek());
                            empty();
                        }//计算
                         else if(tmp == "贷款"){
                             cl.show(Cardall,"loans");
                             empty();
                            infoAll = traverse(view);
                            info.setText(infoAll);
                        }
                    }//运算整过程
                }
        );
    }//获取数值
    public void inTranSuffix(ArrayList<String> num){
        for(String info:num){
            if(!judgeSeek(info,basicSign)&&info!="("&&info!=")"){ numList.add(info);}
            else if(judgeSeek(info,basicSign)&&info!="="){
                judgePriority(info,stack);
            }
            else if(info == ")")
            {
                checkBracket();
            }
            else if(info == "(")
            {
                stack.push(info);
            }
        }
        deleStack();
    }//中缀转后缀
    public void empty(){
        emptyStack(stack);
        emptyStack(suffixStack);
        view.clear();
        numList.clear();
        tidy.clear();
    }//清空
    public void emptyStack(Stack stack){
        while(!stack.empty()){
            stack.pop();
        }
    }//清空栈
    public void suffixCal(){
        for(String info:numList){
            if(!judgeSeek(info,basicSign)){suffixStack.push(info);}
            else
            {
                if(!judgeSeek(info,monocular)) {//判断单目还是双目
                    double b = Double.parseDouble(String.valueOf(suffixStack.pop()));
                    double a = Double.parseDouble(String.valueOf(suffixStack.pop()));
                    double s = cal(info, a, b);
                    suffixStack.push(s);
                }
                else {
                    double a = Double.parseDouble(String.valueOf(suffixStack.pop()));
                    double s = calEx(info,a);
                    suffixStack.push(s);
                }
            }
        }
    }//后缀计算法
    public double calEx(String str,double a){
        double s=0;
        switch (str)
        {
            case "^-1":s = 1/a;break;
            case "√":s = Math.sqrt(a);break;
            case "sin":{double b = Math.toRadians(a);s = Math.sin(b);}break;
            case "cos":{double b = Math.toRadians(a);s = Math.cos(b);}break;
            case "tan":{double b = Math.toRadians(a);s = Math.tan(b);}break;
        }//switch判断
        return s;
    }//单目运算符的运算
    public double cal(String str,double a,double b){
        double s=0;
        switch (str)
        {
            case "+":s = a+b;break;
            case "-":s = a-b;break;
            case "*":s = a*b;break;
            case "/":s = a/b;break;
            case "%":s = a%b;break;
        }//switch判断加减乘除符号
        return s;
    }//加减乘除的运算
    public void deleStack(){
        while (!stack.empty()){
            numList.add(String.valueOf(stack.peek()));
            stack.pop();
        }
    }//最后的清理栈
    public void tidyNum(){
        String tmp;
        String tmp2;
        String num;
        tidy.add("W");
        for (int i = 0;i<view.size();i++){
            tmp = view.get(i);
            if(!judgeSeek(tmp,basicNum)) {
                num = "";
                for (int o = i - 1; o >= 0; o--) {
                    tmp2 = view.get(o);
                    if (judgeSeek(tmp2, basicSign) || tmp2 == "(" || tmp2 == ")" || tmp2 == "W") break;
                    else num = num + tmp2;
                }
                if (num != "") {
                    tidy.add(new StringBuffer(num).reverse().toString());
                }
                tidy.add(tmp);
            }
        }
        tidy.remove("W");
    }//整理数列中的数字
    public void checkBracket(){
        String tem;
        while (!stack.empty()){
            tem = String.valueOf(stack.peek());
            if(tem == "("){
                stack.pop();
                return ;
            }
            else {
                numList.add(tem);
                stack.pop();
            }
        }
    }//检查（号
    public void judgePriority(String str,Stack stack){
        String tem;
            while(!stack.empty()) {
                tem = String.valueOf(stack.peek());//栈顶元素
                if (tem != "") {
                    Integer a = priority.get(str);
                    Integer b = priority.get(tem);
                    if (a < b) {
                        numList.add(tem);
                        stack.pop();
                    } else {
                        stack.push(str);
                        return ;
                    }
                }
            }
            stack.push(str);
            return ;
    }//判断栈内是否有高于str优先级的符号
    public void initBasicSign(){
        basicSign.add("+");basicSign.add("-");basicSign.add("*");basicSign.add("/");basicSign.add("=");basicSign.add("%");basicSign.add("^-1");basicSign.add("√");basicSign.add("sin");basicSign.add("cos");basicSign.add("tan");
    }//给基础运算符号初始化
    public void initMonocular(){
        monocular.add("^-1");monocular.add("√");monocular.add("sin");monocular.add("cos");monocular.add("tan");
    }
    public void initBasicNum(){
       basicNum.add("0");basicNum.add("1");basicNum.add("2");basicNum.add("3");basicNum.add("4");basicNum.add("5");basicNum.add("6");basicNum.add("7");basicNum.add("8");basicNum.add("9");
    }//给基础数字初始化
    public boolean judgeSeek(String str, ArrayList<String> seek){
        for(String sign:seek){
            if(sign == str) return true;
        }
        return false;
    }//判断数组中是否存在存入的数据
    public String traverse(ArrayList<String> array){
        String tem ="";
        for(String tmp:array){
            tem = tem + tmp;
        }
        return tem;
    }//遍历数组获取数值
    public void addPriority(){
        priority.put("*",3);
        priority.put("/",3);
        priority.put("+",2);
        priority.put("-",2);
        priority.put("(",1);
        priority.put("%",3);
        priority.put("^-1",3);
        priority.put("√",3);
        priority.put("sin",3);
        priority.put("cos",3);
        priority.put("tan",3);
    }//添加优先级
    static public  ArrayList<String> tidy = new ArrayList<>();//整理后数字组合起来的数组
    static public Stack suffixStack = new Stack();//后缀计算栈
    static public ArrayList<String> view = new ArrayList<>();//单纯显示
    static public ArrayList<String> basicSign = new ArrayList<>();//基础运算符号
    static public ArrayList<String> monocular = new ArrayList<>();//单目运算符号
    static public ArrayList<String> basicNum = new ArrayList<>();//基础数字
    static public Stack stack = new Stack();//栈
    static public ArrayList<String> numList = new ArrayList<>();//后缀
    static public Map<String,Integer> priority = new HashMap<>();//优先级的Map
    static public String[] sign = {"7","8","9","+","Del","√","sin","4","5","6","-","CE","^-1","cos","1","2","3","*","(","%","tan","0",".","=","/",")","贷款"};//按钮对应数值
    static private JButton buList = new JButton();//按钮样式
    private JFrame calAll = new JFrame("计算器 作者：王逸鸣");//主容器
    private JPanel Cardall = new JPanel(new CardLayout());//总卡片
    private JPanel calculateCard = new JPanel(new BorderLayout());//计算器卡片
    private JPanel loansCard = new JPanel(new GridLayout(8,1,2,8));//贷款卡片
    private JPanel numberScr = new JPanel();//显示数字屏
    private JPanel buttonScr = new JPanel();//显示按钮屏
    private JLabel screen = new JLabel();//显示计算公式
    private JLabel infixScreen = new JLabel();//中缀表达式显示屏
    private JLabel suffixScreen = new JLabel();//后缀表达式显示屏
    private CardLayout cl = (CardLayout)(Cardall.getLayout());//卡片集
    private JRadioButton rent = new JRadioButton("等额本息",true);
    private JRadioButton principal = new JRadioButton("等额本金");
    private JLabel mode = new JLabel("还款方式");
    private JPanel tmpPanel1= new JPanel();//第一层
    private JPanel tmpPanel2 = new JPanel();//第二层
    private String[] jlText = {"贷款年限（年）：","贷款金额（万元）：","贷款利率（%）：","月均还款（元）：","利息总额（元）：","还款总额（元）："};
    private double[] loansData = new double[6];//存放贷款的各项数据
    private JButton count = new JButton("计算");
    private JButton reStart = new JButton("重新开始");
    private JButton retu = new JButton("返回上一层");
    private boolean rentIsClicked = true;
    private boolean prinIsClicked;
    private Integer countRent;
    private JTextField[] textFiled = new JTextField[6];
    private ButtonGroup group = new ButtonGroup();//按钮组
    private String tmp = "";
    private String lastAns= "Null";//上次的得数
    private JLabel lastScr = new JLabel(lastAns);
    private double calRentMonth(){
        double tmp1;
        double tmp2;
        double money = loansData[1]*10000;
        double yearLimit = loansData[0];
        double rent = loansData[2]*0.01;
        tmp1 = money*rent*square(1+rent,yearLimit*12);
        tmp2 = square(1+rent,12*yearLimit)-1;
        return tmp1/tmp2;
    }//等额本息的月均还款
    private double calPrinAll(){
        double money = loansData[1]*10000;
        double month = loansData[0]*12;
        double rent = loansData[2]*0.01;
        double tmp1;
        tmp1 = (money/month+money*rent)+money/month*(1+rent);
        return tmp1/2*month-money;
    }//等额本金的总利息
    private void loansEmpty(){
        String empty = "";
        for(int i = 0;i<6;i++)
        {
            loansData[i] = 0;
            textFiled[i].setText(empty);
        }
    }
    private double square(double num,double squ){
        double s =1;
            for(int i =0;i<squ;i++)
            {
                s = s *num;
            }
            return s;
    }//平方快捷函数
    private void buildTextField(JPanel jp,JLabel jl,JTextField jtf,String str){
        jp.setLayout(new FlowLayout());
        jl.setText(str);
        jp.add(jl);
        jp.add(jtf);
        loansCard.add(jp);
    }//创建一个文字后排textfield布局
    private void buildLoans(){
        tmpPanel1.add(mode);
        tmpPanel1.add(rent);
        tmpPanel1.add(principal);
        loansCard.add(tmpPanel1);
        group.add(rent);
        group.add(principal);
        reStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loansEmpty();
            }
        });//重新开始按钮监听器
        count.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i =0;i<3;i++)
                {
                    loansData[i] = Double.parseDouble(textFiled[i].getText());
                }
                if(rentIsClicked){
                    calRentMonth();
                    loansData[3] = calRentMonth();
                    textFiled[3].setText(String.valueOf(loansData[3]));
                    loansData[5] = loansData[3]*12*loansData[0];
                    textFiled[5].setText(String.valueOf(loansData[5]));
                    loansData[4] = loansData[5]-loansData[1]*10000;
                    textFiled[4].setText(String.valueOf(loansData[4]));
                }
                else if(prinIsClicked){
                    loansData[5] = calPrinAll()+(loansData[1]*10000);
                    textFiled[5].setText(String.valueOf(loansData[5]));
                    loansData[3] = loansData[5]/(loansData[0]*12);
                    textFiled[3].setText(String.valueOf(loansData[3]));
                    loansData[4] = calPrinAll()/(loansData[0]*12);
                    textFiled[4].setText(String.valueOf(loansData[4]));

                }
                for(double a:loansData)
                System.out.println(a);
            }
        });
        retu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Cardall,"cal");
            }
        });//返回点击事件
        rent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rentIsClicked = rent.isSelected();
                if(rentIsClicked){
                    prinIsClicked = false;
                }
                System.out.println(rentIsClicked);
                System.out.println(prinIsClicked);
            }
        });//等额本息点击事件
        principal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prinIsClicked = principal.isSelected();
                if(prinIsClicked){
                    rentIsClicked =false;
                }
                System.out.println(rentIsClicked);
                System.out.println(prinIsClicked);
            }
        });//等额本金点击事件
        for(int i = 0;i<6;i++){
            tmp = "";
            countRent = i;
            JPanel tmpPanel = new JPanel();
            JLabel tmpLabel = new JLabel();
            JTextField tmpJTF = new JTextField(18);
            textFiled[i] = tmpJTF;
            buildTextField(tmpPanel,tmpLabel,textFiled[countRent],jlText[i]);
        }
        tmpPanel2.add(count);
        tmpPanel2.add(reStart);
        tmpPanel2.add(retu);
        loansCard.add(tmpPanel2);
    }//贷款卡片添加组件
    public void Build(){
        for(String info:sign){
            buList = new JButton(info);
            buttonScr.add(buList);
            getValue(buList,screen);
        }//构造所有按钮
        calAll.add(Cardall);
        Cardall.add(loansCard,"loans");
        Cardall.add(calculateCard,"cal");
        numberScr.setLayout(new GridLayout(4,1,5,5));
        numberScr.setBorder(BorderFactory.createLineBorder(Color.black));
        numberScr.add(lastScr);
        numberScr.add(screen,BorderLayout.CENTER);
        numberScr.add(infixScreen);
        numberScr.add(suffixScreen);
        calculateCard.add(numberScr,BorderLayout.CENTER);
        calculateCard.add(buttonScr, BorderLayout.SOUTH);
        cl.show(Cardall,"cal");
        calAll.setVisible(true);
        calAll.setResizable(false);
        calAll.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildLoans();
    }
    public calPage(){
        calAll.setBounds(300,200,500,350);
        buttonScr.setLayout(new GridLayout(4,6,5,5));
        addPriority();//初始化
        initBasicSign();
        initBasicNum();
        initMonocular();
        Build();
    }//构造
}