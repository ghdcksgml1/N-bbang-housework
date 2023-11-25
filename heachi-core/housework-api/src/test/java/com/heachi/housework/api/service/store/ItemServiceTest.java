package com.heachi.housework.api.service.store;

import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.store.request.ItemAddServiceRequest;
import com.heachi.housework.api.service.store.response.ItemSelectResponse;
import com.heachi.mysql.define.store.Item;
import com.heachi.mysql.define.store.constant.ItemStatus;
import com.heachi.mysql.define.store.repository.ItemRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemServiceTest extends TestConfig {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품 등록 성공 테스트")
    void itemAddSuccessTest() {
        User user = userRepository.save(generateUser());

        // given
        ItemAddServiceRequest request = ItemAddServiceRequest.builder()
                .email(user.getEmail())
                .file("google.com")
                .name("test")
                .content("test")
                .price(3000)
                .build();

        // when
        Item saveItem = itemService.addItem(request);

        // then
        Item item = itemRepository.findByItemIdJoinFetchUser(saveItem.getId());
        assertThat(item.getUser().getName()).isEqualTo(user.getName());
        assertThat(item.getUser().getEmail()).isEqualTo(user.getEmail());
        assertThat(item.getItemStatus()).isEqualTo(ItemStatus.ITEM_AVAILABLE);
        assertThat(item.getName()).isEqualTo(request.getName());
        assertThat(item.getContent()).isEqualTo(request.getContent());
        assertThat(item.getPrice()).isEqualTo(request.getPrice());
        assertThat(item.getItemImageURL()).isEqualTo(request.getFile());
    }

    @Test
    @DisplayName("상품 삭제 성공 테스트")
    void itemDeleteSuccessTest() {
        User user = userRepository.save(generateUser());

        Item saveItem = itemRepository.save(Item.builder()
                .user(user)
                .name("test")
                .content("test")
                .price(3000)
                .itemImageURL("google.com")
                .build());

        // when
        itemService.deleteItem(saveItem.getId(), user.getEmail());

        // then
        assertThat(itemRepository.findById(saveItem.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("상품 리스트 조회 성공 테스트")
    void selectItemListSuccessTest() {
        User user = userRepository.save(generateUser());

        Item saveItem1 = itemRepository.save(Item.builder()
                .user(user)
                .name("test")
                .content("test")
                .price(3000)
                .itemImageURL("google.com")
                .build());

        Item saveItem2 = itemRepository.save(Item.builder()
                .user(user)
                .name("test2")
                .content("test2")
                .price(6000)
                .itemImageURL("google2.com")
                .build());

        // when
        List<ItemSelectResponse> itemList = itemService.selectItemList();

        // then
        assertThat(itemList.get(0).getName()).isEqualTo("test");
        assertThat(itemList.get(1).getName()).isEqualTo("test2");

        assertThat(itemList.get(0).getPrice()).isEqualTo(3000);
        assertThat(itemList.get(1).getPrice()).isEqualTo(6000);

        assertThat(itemList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 조회 성공 테스트")
    void selectItemSuccessTest() {
        User user = userRepository.save(generateUser());

        Item saveItem = itemRepository.save(Item.builder()
                .user(user)
                .name("test")
                .content("test")
                .price(3000)
                .itemImageURL("google.com")
                .build());

        // when
        ItemSelectResponse response = itemService.selectItem(saveItem.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFile()).isEqualTo(saveItem.getItemImageURL());
        assertThat(response.getPrice()).isEqualTo(saveItem.getPrice());
    }
}