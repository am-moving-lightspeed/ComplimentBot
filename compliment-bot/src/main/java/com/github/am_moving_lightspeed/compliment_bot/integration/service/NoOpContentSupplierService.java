package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class NoOpContentSupplierService implements ContentSupplierService {

    @Override
    public Set<Compliment> getContent() {
        return Set.of();
    }
}
