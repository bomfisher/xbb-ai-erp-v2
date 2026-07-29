package xbb.ai.erp.base.persistence.entity;

public class BaseEntity {

    private Long id;
    private Long addTime;
    private Long updateTime;
    private Integer del;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Integer getDeleted() {
        return del;
    }

    public void setDeleted(Integer deleted) {
        this.del = deleted;
    }
}
