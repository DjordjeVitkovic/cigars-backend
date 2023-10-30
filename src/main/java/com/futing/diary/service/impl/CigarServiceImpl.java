package com.futing.diary.service.impl;

import com.futing.diary.controller.cigar.dto.CigarRequestDTO;
import com.futing.diary.controller.cigar.dto.CigarResponseDTO;
import com.futing.diary.controller.cigar.dto.PageCigarResponseDTO;
import com.futing.diary.exception.BadRequestException;
import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Brand;
import com.futing.diary.model.Cigar;
import com.futing.diary.model.Image;
import com.futing.diary.repository.BrandRepository;
import com.futing.diary.repository.CigarRepository;
import com.futing.diary.repository.ImageRepository;
import com.futing.diary.service.CigarService;
import com.futing.diary.util.CloudinaryUtility;
import com.futing.diary.util.dto.CloudinaryResponseDTO;
import com.futing.diary.util.mapper.CigarMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.futing.diary.util.Constants.ERROR_CIGAR_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = SQLException.class)
public class CigarServiceImpl implements CigarService {

  private final CloudinaryUtility cloudinaryUtility;
  private final CigarRepository cigarRepository;
  private final BrandRepository brandRepository;
  private final ImageRepository imageRepository;
  private final CigarMapper cigarMapper;

  @Override
  public CigarResponseDTO addCigar(MultipartFile[] images, CigarRequestDTO requestDTO) throws IOException {

    Cigar cigarToSave = buildCigarFromRequest(requestDTO);
    Cigar savedCigar = cigarRepository.save(cigarToSave);

    if (images != null)
      savedCigar.setImages(uploadImages(images, cigarToSave));

    return cigarMapper.cigarToCigarResponseDTO(cigarRepository.save(savedCigar));
  }

  @Override
  public CigarResponseDTO getCigar(Integer cigarId) {
    return cigarMapper.cigarToCigarResponseDTO(cigarRepository.findById(cigarId)
      .orElseThrow(() -> new NotFoundException(ERROR_CIGAR_NOT_EXIST)));
  }

  @Override
  public void deleteCigarById(Integer cigarId) {
    Cigar cigar = cigarRepository.findById(cigarId)
      .orElseThrow(() -> new NotFoundException(ERROR_CIGAR_NOT_EXIST));

    imageRepository.findAllById(
        cigar.getImages()
          .stream()
          .map(Image::getImageId)
          .toList())
      .forEach(image -> {
        try {
          cloudinaryUtility.deleteImageFromCloudinary(image.getPublicId());
        } catch (Exception e) {
          log.error("Error while deleting images form cloud.");
          throw new IllegalStateException(e);
        }
      });

    cigarRepository.delete(cigar);
  }

  @Override
  public PageCigarResponseDTO searchCigar(String search, int pageSize, int pageNumber) {
    if (pageSize < 1)
      throw new BadRequestException("Page size must not be less than one");

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<Cigar> allCigars = cigarRepository.searchCigars(search, pageable);

    return PageCigarResponseDTO.builder()
      .cigars(
        allCigars.stream().map(cigarMapper::cigarToCigarResponseDTO).toList())
      .totalElements(allCigars.getTotalElements())
      .totalPages(allCigars.getTotalPages())
      .build();
  }

  private Cigar buildCigarFromRequest(CigarRequestDTO requestDTO) {
    Brand brand = brandRepository.findById(requestDTO.getBrandId())
      .orElseThrow(() -> new NotFoundException("Brand with provided id doesn't exist."));

    return Cigar.builder()
      .name(requestDTO.getName())
      .wrapper(requestDTO.getWrapper())
      .binder(requestDTO.getBinder())
      .filler(requestDTO.getFiller())
      .cigarStrength(requestDTO.getCigarStrength())
      .shape(requestDTO.getShape())
      .rolling(requestDTO.getRolling())
      .agingPotential(requestDTO.isAgingPotential())
      .harvesting(requestDTO.getHarvesting())
      .curing(requestDTO.getCuring())
      .aging(requestDTO.getAging())
      .priceRange(requestDTO.getPriceRange())
      .limitedEdition(requestDTO.isLimitedEdition())
      .historicalBackground(requestDTO.getHistoricalBackground())
      .flavors(requestDTO.getFlavors())
      .fermentation(requestDTO.getFermentation())
      .pairings(requestDTO.getPairings())
      .humidificationStorage(requestDTO.getHumidificationStorage())
      .created(ZonedDateTime.now())
      .brand(brand)
      .build();
  }

  private List<Image> uploadImages(MultipartFile[] images, Cigar cigar) throws IOException {
    List<Image> savedImages = new ArrayList<>();

    for (MultipartFile image : images) {
      CloudinaryResponseDTO cloudinaryResponse = cloudinaryUtility.uploadOnCloudinary(image);
      Image imageToSave = Image.builder()
        .cigar(cigar)
        .created(ZonedDateTime.now())
        .link(cloudinaryResponse.getUrl())
        .publicId(cloudinaryResponse.getPublicId())
        .build();
      savedImages.add(imageToSave);
    }

    return savedImages;
  }
}
