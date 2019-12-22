/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.dto;

import java.util.Objects;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

public class SpecializationDTO {

    private String specializationName;
    private String specializationCode;
    private boolean hasAnySpecialists;

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public boolean isHasAnySpecialists() {
        return hasAnySpecialists;
    }

    public void setHasAnySpecialists(boolean hasAnySpecialists) {
        this.hasAnySpecialists = hasAnySpecialists;
    }

    public SpecializationDTO() {
    }

    public SpecializationDTO(String specializationCode, String specializationName) {
        this.specializationCode = specializationCode;
        this.specializationName = specializationName;
    }

    public SpecializationDTO(String specializationName, String specializationCode, boolean hasAnySpecialists) {
        this.specializationName = specializationName;
        this.specializationCode = specializationCode;
        this.hasAnySpecialists = hasAnySpecialists;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.specializationName);
        hash = 59 * hash + Objects.hashCode(this.specializationCode);
        hash = 59 * hash + (this.hasAnySpecialists ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpecializationDTO other = (SpecializationDTO) obj;
        if (this.hasAnySpecialists != other.hasAnySpecialists) {
            return false;
        }
        if (!Objects.equals(this.specializationName, other.specializationName)) {
            return false;
        }
        if (!Objects.equals(this.specializationCode, other.specializationCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return specializationName + ", " + ContextUtils.printI18NMessage("page.add.specialist.list.code") + ": " + specializationCode;
    }

}
