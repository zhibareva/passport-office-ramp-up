package com.passportoffice.utils;

import com.devskiller.friendly_id.FriendlyId;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

  public String getId() {
    return FriendlyId.createFriendlyId();
  }
}
