package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Content;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class NoOpContentSupplierService implements ContentSupplierService {

    @Override
    public Set<Content> getContent() {
        return Set.of();
    }
}
