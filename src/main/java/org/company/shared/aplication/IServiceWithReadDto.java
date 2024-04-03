package org.company.shared.aplication;

import java.util.List;

public interface IServiceWithReadDto<Dto, EntityKey, ReadDto> extends IService<Dto, EntityKey> {
    List<ReadDto> getAllItemsRead();
}
