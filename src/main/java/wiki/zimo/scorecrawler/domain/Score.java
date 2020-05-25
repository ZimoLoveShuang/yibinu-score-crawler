package wiki.zimo.scorecrawler.domain;

public class Score implements Comparable<Score> {
    String xq;// 学期
    int xqSort;// 学期排序
    String kcmc;// 课程名称
    String kclb;// 课程类别
    String xdzk;// 修读状况
    double xf;// 学分
    int cj;// 成绩
    double jd;// 绩点

    public int getXqSort() {
        return xqSort;
    }

    public void setXqSort(int xqSort) {
        this.xqSort = xqSort;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getKclb() {
        return kclb;
    }

    public void setKclb(String kclb) {
        this.kclb = kclb;
    }

    public String getXdzk() {
        return xdzk;
    }

    public void setXdzk(String xdzk) {
        this.xdzk = xdzk;
    }

    public double getXf() {
        return xf;
    }

    public void setXf(double xf) {
        this.xf = xf;
    }

    public int getCj() {
        return cj;
    }

    public void setCj(int cj) {
        this.cj = cj;
    }

    public double getJd() {
        return jd;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    @Override
    public String toString() {
        return "Score{" +
                "学期=" + xqSort +
                ", 课程名称='" + kcmc + '\'' +
                ", 课程类别='" + kclb + '\'' +
                ", 修读状况='" + xdzk + '\'' +
                ", 学分=" + xf +
                ", 成绩=" + cj +
                ", 绩点=" + jd +
                '}';
    }

    @Override
    public int compareTo(Score o) {
        if (this.xqSort == o.xqSort) {
            if (this.kclb.equals(o.kclb)) {
                return this.kcmc.compareTo(o.kcmc);
            }
            return this.kclb.compareTo(o.kclb);
        }
        return this.xqSort - o.xqSort;
    }
}
