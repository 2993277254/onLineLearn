package com.lsfly.bas.model.course;

import java.io.Serializable;

public class TCourseWithBLOBs extends TCourse implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_course_.summary_
     *
     * @mbg.generated
     */
    private String summary;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_course_.outline_
     *
     * @mbg.generated
     */
    private String outline;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_course_.class_test_
     *
     * @mbg.generated
     */
    private String classTest;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_course_.examination_
     *
     * @mbg.generated
     */
    private String examination;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_course_
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_course_.summary_
     *
     * @return the value of t_course_.summary_
     *
     * @mbg.generated
     */
    public String getSummary() {
        return summary;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_course_.summary_
     *
     * @param summary the value for t_course_.summary_
     *
     * @mbg.generated
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_course_.outline_
     *
     * @return the value of t_course_.outline_
     *
     * @mbg.generated
     */
    public String getOutline() {
        return outline;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_course_.outline_
     *
     * @param outline the value for t_course_.outline_
     *
     * @mbg.generated
     */
    public void setOutline(String outline) {
        this.outline = outline == null ? null : outline.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_course_.class_test_
     *
     * @return the value of t_course_.class_test_
     *
     * @mbg.generated
     */
    public String getClassTest() {
        return classTest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_course_.class_test_
     *
     * @param classTest the value for t_course_.class_test_
     *
     * @mbg.generated
     */
    public void setClassTest(String classTest) {
        this.classTest = classTest == null ? null : classTest.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_course_.examination_
     *
     * @return the value of t_course_.examination_
     *
     * @mbg.generated
     */
    public String getExamination() {
        return examination;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_course_.examination_
     *
     * @param examination the value for t_course_.examination_
     *
     * @mbg.generated
     */
    public void setExamination(String examination) {
        this.examination = examination == null ? null : examination.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_course_
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", summary=").append(summary);
        sb.append(", outline=").append(outline);
        sb.append(", classTest=").append(classTest);
        sb.append(", examination=").append(examination);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_course_
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TCourseWithBLOBs other = (TCourseWithBLOBs) that;
        return (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getPhotoUrl() == null ? other.getPhotoUrl() == null : this.getPhotoUrl().equals(other.getPhotoUrl()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
            && (this.getPersonNum() == null ? other.getPersonNum() == null : this.getPersonNum().equals(other.getPersonNum()))
            && (this.getTarget() == null ? other.getTarget() == null : this.getTarget().equals(other.getTarget()))
            && (this.getDemand() == null ? other.getDemand() == null : this.getDemand().equals(other.getDemand()))
            && (this.getTeacherId() == null ? other.getTeacherId() == null : this.getTeacherId().equals(other.getTeacherId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getOrderBy() == null ? other.getOrderBy() == null : this.getOrderBy().equals(other.getOrderBy()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getVerifyTime() == null ? other.getVerifyTime() == null : this.getVerifyTime().equals(other.getVerifyTime()))
            && (this.getCommitTime() == null ? other.getCommitTime() == null : this.getCommitTime().equals(other.getCommitTime()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getOutline() == null ? other.getOutline() == null : this.getOutline().equals(other.getOutline()))
            && (this.getClassTest() == null ? other.getClassTest() == null : this.getClassTest().equals(other.getClassTest()))
            && (this.getExamination() == null ? other.getExamination() == null : this.getExamination().equals(other.getExamination()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_course_
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getPhotoUrl() == null) ? 0 : getPhotoUrl().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getPersonNum() == null) ? 0 : getPersonNum().hashCode());
        result = prime * result + ((getTarget() == null) ? 0 : getTarget().hashCode());
        result = prime * result + ((getDemand() == null) ? 0 : getDemand().hashCode());
        result = prime * result + ((getTeacherId() == null) ? 0 : getTeacherId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getOrderBy() == null) ? 0 : getOrderBy().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getVerifyTime() == null) ? 0 : getVerifyTime().hashCode());
        result = prime * result + ((getCommitTime() == null) ? 0 : getCommitTime().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getOutline() == null) ? 0 : getOutline().hashCode());
        result = prime * result + ((getClassTest() == null) ? 0 : getClassTest().hashCode());
        result = prime * result + ((getExamination() == null) ? 0 : getExamination().hashCode());
        return result;
    }
}