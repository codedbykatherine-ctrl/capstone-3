package org.yearup.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class ProfileController {
    private ProfileService profileService;
    private UserService userService;


    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getProfile(Principal principal) {

        // Get the logged-in user's username from the JWT token.
        String userName = principal.getName();

        // Use the username to find the user id.
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        // Return the profile that belongs to this user.
        return profileService.getByUserId(userId);
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal) {

        // Get the logged-in user's username from the JWT token.
        String userName = principal.getName();


        User user = userService.getByUserName(userName);
        int userId = user.getId();
//         COMMENTS
        return profileService.update(userId, profile);
    }
}


