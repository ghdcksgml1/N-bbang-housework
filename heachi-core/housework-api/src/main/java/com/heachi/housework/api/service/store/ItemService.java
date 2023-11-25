package com.heachi.housework.api.service.store;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.item.ItemException;
import com.heachi.admin.common.exception.user.UserException;
import com.heachi.housework.api.service.store.request.ItemAddServiceRequest;
import com.heachi.housework.api.service.store.response.ItemSelectResponse;
import com.heachi.mysql.define.store.Item;
import com.heachi.mysql.define.store.repository.ItemRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Item addItem(ItemAddServiceRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.warn(">>>> User Not FOUND : {}", ExceptionMessage.USER_NOT_FOUND.getText());
            throw new UserException(ExceptionMessage.USER_NOT_FOUND);
        });

        // Item 저장
        return itemRepository.save(Item.builder()
                .user(user)
                .name(request.getName())
                .content(request.getContent())
                .price(request.getPrice())
                .itemImageURL(request.getFile())
                .build());
    }

    public List<ItemSelectResponse> selectItemList() {
        // Item 전부 조회
        return itemRepository.findAll().stream()
                .map(i -> ItemSelectResponse.builder()
                        .name(i.getName())
                        .content(i.getContent())
                        .file(i.getItemImageURL())
                        .price(i.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteItem(Long itemId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn(">>>> User Not FOUND : {}", ExceptionMessage.USER_NOT_FOUND.getText());
            throw new UserException(ExceptionMessage.USER_NOT_FOUND);
        });

        // Item 조회
        Item findItem = itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn(">>>> Not Found ITEM");

            throw new ItemException(ExceptionMessage.ITEM_NOT_FOUND);
        });

        // 요청자와 판매자가 일치하지 않을 경우 예외 발생
        if (!user.equals(findItem.getUser())) {
            log.warn(">>>> Not Unauthorized");
            throw new ItemException(ExceptionMessage.ITEM_UNAUTHORIZED);
        }

        itemRepository.deleteById(findItem.getId());
    }

    public ItemSelectResponse selectItem(Long itemId) {
        // Item 조회
        Item findItem = itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn(">>>> Not Found ITEM");

            throw new ItemException(ExceptionMessage.ITEM_NOT_FOUND);
        });

        return ItemSelectResponse.builder()
                .name(findItem.getName())
                .content(findItem.getContent())
                .file(findItem.getItemImageURL())
                .price(findItem.getPrice())
                .build();
    }
}
