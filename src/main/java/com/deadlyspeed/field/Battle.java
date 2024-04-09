package com.deadlyspeed.field;

public interface Battle<T extends Battle> {
  T createField();
  T putFighters();
}
