package com.lsfly.bas.model.system;

import java.io.Serializable;
import java.util.Date;

public class SysDictData implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.dict_id
     *
     * @mbg.generated
     */
    private String dictId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.dict_data_name
     *
     * @mbg.generated
     */
    private String dictDataName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.dict_data_value
     *
     * @mbg.generated
     */
    private String dictDataValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.dict_data_sort
     *
     * @mbg.generated
     */
    private Long dictDataSort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.dict_data_desc
     *
     * @mbg.generated
     */
    private String dictDataDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.is_delete_
     *
     * @mbg.generated
     */
    private String isDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.version_
     *
     * @mbg.generated
     */
    private Long version;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.create_by_
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.create_time_
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.update_by_
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dict_data.update_time_
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_dict_data
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.id
     *
     * @return the value of sys_dict_data.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.id
     *
     * @param id the value for sys_dict_data.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.dict_id
     *
     * @return the value of sys_dict_data.dict_id
     *
     * @mbg.generated
     */
    public String getDictId() {
        return dictId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.dict_id
     *
     * @param dictId the value for sys_dict_data.dict_id
     *
     * @mbg.generated
     */
    public void setDictId(String dictId) {
        this.dictId = dictId == null ? null : dictId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.dict_data_name
     *
     * @return the value of sys_dict_data.dict_data_name
     *
     * @mbg.generated
     */
    public String getDictDataName() {
        return dictDataName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.dict_data_name
     *
     * @param dictDataName the value for sys_dict_data.dict_data_name
     *
     * @mbg.generated
     */
    public void setDictDataName(String dictDataName) {
        this.dictDataName = dictDataName == null ? null : dictDataName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.dict_data_value
     *
     * @return the value of sys_dict_data.dict_data_value
     *
     * @mbg.generated
     */
    public String getDictDataValue() {
        return dictDataValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.dict_data_value
     *
     * @param dictDataValue the value for sys_dict_data.dict_data_value
     *
     * @mbg.generated
     */
    public void setDictDataValue(String dictDataValue) {
        this.dictDataValue = dictDataValue == null ? null : dictDataValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.dict_data_sort
     *
     * @return the value of sys_dict_data.dict_data_sort
     *
     * @mbg.generated
     */
    public Long getDictDataSort() {
        return dictDataSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.dict_data_sort
     *
     * @param dictDataSort the value for sys_dict_data.dict_data_sort
     *
     * @mbg.generated
     */
    public void setDictDataSort(Long dictDataSort) {
        this.dictDataSort = dictDataSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.dict_data_desc
     *
     * @return the value of sys_dict_data.dict_data_desc
     *
     * @mbg.generated
     */
    public String getDictDataDesc() {
        return dictDataDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.dict_data_desc
     *
     * @param dictDataDesc the value for sys_dict_data.dict_data_desc
     *
     * @mbg.generated
     */
    public void setDictDataDesc(String dictDataDesc) {
        this.dictDataDesc = dictDataDesc == null ? null : dictDataDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.is_delete_
     *
     * @return the value of sys_dict_data.is_delete_
     *
     * @mbg.generated
     */
    public String getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.is_delete_
     *
     * @param isDelete the value for sys_dict_data.is_delete_
     *
     * @mbg.generated
     */
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.version_
     *
     * @return the value of sys_dict_data.version_
     *
     * @mbg.generated
     */
    public Long getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.version_
     *
     * @param version the value for sys_dict_data.version_
     *
     * @mbg.generated
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.create_by_
     *
     * @return the value of sys_dict_data.create_by_
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.create_by_
     *
     * @param createBy the value for sys_dict_data.create_by_
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.create_time_
     *
     * @return the value of sys_dict_data.create_time_
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.create_time_
     *
     * @param createTime the value for sys_dict_data.create_time_
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.update_by_
     *
     * @return the value of sys_dict_data.update_by_
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.update_by_
     *
     * @param updateBy the value for sys_dict_data.update_by_
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dict_data.update_time_
     *
     * @return the value of sys_dict_data.update_time_
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dict_data.update_time_
     *
     * @param updateTime the value for sys_dict_data.update_time_
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dict_data
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dictId=").append(dictId);
        sb.append(", dictDataName=").append(dictDataName);
        sb.append(", dictDataValue=").append(dictDataValue);
        sb.append(", dictDataSort=").append(dictDataSort);
        sb.append(", dictDataDesc=").append(dictDataDesc);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", version=").append(version);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dict_data
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
        SysDictData other = (SysDictData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDictId() == null ? other.getDictId() == null : this.getDictId().equals(other.getDictId()))
            && (this.getDictDataName() == null ? other.getDictDataName() == null : this.getDictDataName().equals(other.getDictDataName()))
            && (this.getDictDataValue() == null ? other.getDictDataValue() == null : this.getDictDataValue().equals(other.getDictDataValue()))
            && (this.getDictDataSort() == null ? other.getDictDataSort() == null : this.getDictDataSort().equals(other.getDictDataSort()))
            && (this.getDictDataDesc() == null ? other.getDictDataDesc() == null : this.getDictDataDesc().equals(other.getDictDataDesc()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dict_data
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDictId() == null) ? 0 : getDictId().hashCode());
        result = prime * result + ((getDictDataName() == null) ? 0 : getDictDataName().hashCode());
        result = prime * result + ((getDictDataValue() == null) ? 0 : getDictDataValue().hashCode());
        result = prime * result + ((getDictDataSort() == null) ? 0 : getDictDataSort().hashCode());
        result = prime * result + ((getDictDataDesc() == null) ? 0 : getDictDataDesc().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}