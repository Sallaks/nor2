package com.mycompany.myapp.service.dto;

public class DevsBySkillDTO {

    private String skillName;

    private Integer count;

    public String getSkillName() {
        return skillName;
    }

    public DevsBySkillDTO(String skillName, Integer count) {
        this.skillName = skillName;
        this.count = count;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((count == null) ? 0 : count.hashCode());
        result = prime * result + ((skillName == null) ? 0 : skillName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevsBySkillDTO other = (DevsBySkillDTO) obj;
        if (count == null) {
            if (other.count != null)
                return false;
        } else if (!count.equals(other.count))
            return false;
        if (skillName == null) {
            if (other.skillName != null)
                return false;
        } else if (!skillName.equals(other.skillName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DTODevsBySkill [count=" + count + ", skillName=" + skillName + "]";
    }

}
