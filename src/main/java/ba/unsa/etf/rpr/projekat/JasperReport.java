package ba.unsa.etf.rpr.projekat;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class JasperReport extends JFrame {
    public void showReport(Connection conn) throws JRException {
        String reportSrcFile = getClass().getResource("/reports/Database.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();
        net.sf.jasperreports.engine.JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("reportsDirPath", reportsDir);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(1300, 800);
        this.setVisible(true);
    }
}
