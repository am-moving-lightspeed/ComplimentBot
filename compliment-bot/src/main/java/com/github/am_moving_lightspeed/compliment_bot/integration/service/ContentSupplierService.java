package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import java.util.Set;

public interface ContentSupplierService {

    /**
     * Hash to content mapping
     */
    Set<Compliment> getContent();
}
