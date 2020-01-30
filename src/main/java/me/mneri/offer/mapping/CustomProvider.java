package me.mneri.offer.mapping;

import lombok.SneakyThrows;
import me.mneri.offer.entity.Offer;
import org.modelmapper.Provider;

class CustomProvider implements Provider<Object> {
    @Override
    @SneakyThrows
    public Object get(ProvisionRequest<Object> request) {
        Class<?> type = request.getRequestedType();

        if (type.equals(Offer.class)) {
            return Offer.builder().build();
        } else {
            return null;
        }
    }
}
