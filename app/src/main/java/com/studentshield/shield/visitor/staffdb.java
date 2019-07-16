package com.studentshield.shield.visitor;

/**
 * Created by hhh on 9/4/2018.
 */

public class staffdb {


    private String staffname;
    private String staffdesig;
    private String staffmobile;
    private String staffemail;


    public staffdb(String staffname,String staffdesig,String staffmobile,String staffemail)
    {

        this.staffname=staffname;
        this.staffdesig=staffdesig;
        this.staffmobile=staffmobile;
        this.staffemail=staffemail;

    }

    public String getStaffname()
    {
        return staffname;
    }
    public String getStaffdesig()
    {
        return staffdesig;
    }
    public String getStaffmobile()
    {
        return staffmobile;
    }
    public String getStaffemail()
    {
        return staffemail;
    }





    public void setStaffname(String staffname)
    {
        this.staffname=staffname;
    }
    public void setStaffdesig(String staffdesig)
    {
        this.staffdesig=staffdesig;
    }


    public void setStaffmobile(String staffemail)
    {
        this.staffemail=staffemail;
    }
    public void setStaffemail(String staffemail)
    {
        this.staffemail=staffemail;
    }

}
