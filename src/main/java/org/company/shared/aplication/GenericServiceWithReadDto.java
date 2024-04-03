package org.company.shared.aplication;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @param <Entity>
 *     This class has to have non-argument constructor, otherwise after saveItem is called, exception will be thrown
 * @param <Dto>
 * @param <ReadDto>
 * @param <Repository>
 * @param <Assembler>
 * @param <ReadAssembler>
 */
public class GenericServiceWithReadDto<Entity, Dto, ReadDto, EntityKey,
        Repository extends JpaRepository<Entity, EntityKey>,
        Assembler extends IAssembler<Entity,Dto>,
        ReadAssembler extends IAssembler<Entity,ReadDto>>
    extends GenericService<Entity, Dto, EntityKey, Repository, Assembler>
    implements IServiceWithReadDto<Dto, EntityKey, ReadDto> {
    
    protected final ReadAssembler readAssembler;
    
    public GenericServiceWithReadDto(Repository repository, Assembler assembler, ReadAssembler readAssembler) {
        super(repository,assembler);
        this.readAssembler = readAssembler;
    }

    public List<ReadDto> getAllItemsRead() {
        return repository.findAll()
                .stream()
                .map(readAssembler::toDto)
                .collect(Collectors.toList());
    }
}
