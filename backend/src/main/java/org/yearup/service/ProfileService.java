package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Profile getByUserId(int userId) {
        // Find the profile that belongs to this user.
        return profileRepository.findByUserId(userId);
    }

    public Profile update(int userId, Profile profile) {

        // Look up the current profile from the database.
        Profile existing = profileRepository.findByUserId(userId);

        if (existing == null) {
            return null;
        }

        // Copy the updated profile fields onto the existing profile.
        existing.setFirstName(profile.getFirstName());
        existing.setLastName(profile.getLastName());
        existing.setPhone(profile.getPhone());
        existing.setEmail(profile.getEmail());
        existing.setAddress(profile.getAddress());
        existing.setCity(profile.getCity());
        existing.setState(profile.getState());
        existing.setZip(profile.getZip());

        // Save and return the updated profile.
        return profileRepository.save(existing);
    }
}
