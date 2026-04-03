package domain.action;

public class SpecialSkillAction implements Action {
    private final String skillName;

    public SpecialSkillAction(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public String getName() {
        return skillName;
    }
}
