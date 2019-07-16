package com.studentshield.shield.visitor;

/**
 * Created by hhh on 9/4/2018.
 */

public class visitordb {


    private String uid;
    private String visitorname;
    private String visitormobile;
    private String visitorpurp;
    private String visitorimage;
    private String visitorimageid;
    private String visitoremailid;
    private String visitordesig;
    private String visitoraddr;

    public visitordb(String uid,String visitorname,String visitormobile,String visitorpurp,String visitorimage,String visitorimageid,String visitoremailid,String visitordesig,String visitoraddr)
    {
        this.uid=uid;
        this.visitorname=visitorname;
        this.visitormobile=visitormobile;
        this.visitorpurp=visitorpurp;
        this.visitorimage=visitorimage;
        this.visitorimageid=visitorimageid;
        this.visitoremailid=visitoremailid;
        this.visitordesig=visitordesig;
        this.visitoraddr=visitoraddr;
    }

    public String getUid()
    {
        return uid;
    }
    public String getVisitorname()
    {
        return visitorname;
    }
    public String getVisitormobile()
    {
        return visitormobile;
    }
    public String getVisitorpurp()
    {
        return visitorpurp;
    }
    public String getVisitorimage()
    {
        return visitorimage;
    }
    public String getVisitorimageid()
    {
        return visitorimageid;
    }
    public String getVisitoremailid()
    {
        return visitoremailid;
    }
    public String getVisitordesig()
    {
        return visitordesig;
    }
    public String getVisitoraddr()
    {
        return visitoraddr;
    }



    public void setUid(String uid)
    {
        this.uid=uid;
    }
    public void setVisitorname(String visitorname)
    {
        this.visitorname=visitorname;
    }
    public void setVisitormobile(String visitormobile)
    {
        this.visitormobile=visitormobile;
    }
    public void setVisitorpurp(String visitorpurp)
    {
        this.visitorpurp=visitorpurp;
    }
    public void setVisitorimage(String visitorimage)
    {
        this.visitorimage=visitorimage;
    }
    public void setVisitorimageid(String visitorimageid)
    {
        this.visitorimageid=visitorimageid;
    }
    public void setVisitoremailid(String visitoremailid)
    {
        this.visitoremailid=visitoremailid;
    }
    public void setVisitordesig(String visitordesig)
    {
        this.visitordesig=visitordesig;
    }
    public void setVisitoraddr(String visitoraddr)
    {
        this.visitoraddr=visitoraddr;
    }
}
