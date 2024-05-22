package kr.co.softhubglobal.dto.store;

import kr.co.softhubglobal.entity.store.StoreRepresentative;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StoreRepresentativeInfoMapper implements Function<StoreRepresentative, StoreDTO.StoreRepresentativeInfo> {

    @Override
    public StoreDTO.StoreRepresentativeInfo apply(StoreRepresentative storeRepresentative) {
        return new StoreDTO.StoreRepresentativeInfo(
                storeRepresentative.getStore().getBusinessName(),
                storeRepresentative.getUser().getUsername(),
                storeRepresentative.getUser().getName(),
                storeRepresentative.getUser().getPhoneNumber(),
                storeRepresentative.getUser().getRegisteredDate(),
                storeRepresentative.getStore().getStatus()
        );
    }
}
