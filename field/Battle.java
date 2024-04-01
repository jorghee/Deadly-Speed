package field;

public interface Battle<T extends Battle> {
  T createField();
  T putWarriors();
}
