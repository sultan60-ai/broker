package de.sz.accounts.service;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) { super(message); }
}
