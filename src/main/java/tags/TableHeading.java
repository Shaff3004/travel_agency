package tags;

import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableHeading extends SimpleTagSupport {
    private String values;
    private String lang;

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public void doTag() throws JspException, IOException {
        Locale locale = null;
        if (getLang().equals("ru")) {
            locale = new Locale("ru");
        } else {
            locale = new Locale("en");
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources",locale);

        String[] values = getValues().split(",");
        JspWriter out = getJspContext().getOut();
        out.println("<tr>");
        for(int i = 0; i < values.length; i++){
            String head = rb.getString(values[i]);
            if(head.equals("Action") || head.equals("Действие")){
                out.println("<th>" + head + "</th>");
            } else {
                out.println("<th>" + head + " <i class=\"fa fa-sort\"></i></th>");
            }
        }
        out.println("</tr>");
    }
}
