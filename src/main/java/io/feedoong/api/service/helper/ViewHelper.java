package io.feedoong.api.service.helper;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.view.View;
import io.feedoong.api.domain.view.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ViewHelper {
    private final ViewRepository viewRepository;

    public Optional<View> findOptionalItemView(User user, Item item) {
        return viewRepository.findViewByUserAndItemAndDeletedAtIsNull(user, item);
    }

    public void save(View view) {
        viewRepository.save(view);
    }
}
