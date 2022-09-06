using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{

    //Experimental
    public Animator anima;
    public CharacterController controller;
    public float speed = 4.5f;
    public float walkSpeed = 4.5f;
    public float sprintSpeed = 8f;
    public float gravity = -9.81f;
    public float jumpHeight = 3f;
    
    public Transform groundCheck;
    public float groundDistance = 0.4f;
    public LayerMask groundMask;
    Vector3 velocity;
    bool isGrounded;
    
    // Update is called once per frame
    void Update()
    {
        isGrounded = Physics.CheckSphere(groundCheck.position, groundDistance, groundMask);

        if(isGrounded && velocity.y < 0)
        {
            velocity.y = -2f;
        }

        //Sivuttain
        float x = Input.GetAxis("Horizontal");
        //Eteenpäin
        float z = Input.GetAxis("Vertical");
        //float sprintV = 1f;


        /*if(Input.GetButtonDown("Sprint")){
            sprintV = sprintV*2;
        }*/
        if(Input.GetKey(KeyCode.LeftShift)){
            speed = sprintSpeed;
        }
        else{
            speed = walkSpeed;
        }

        Vector3 move = transform.right * x + transform.forward * z;

        controller.Move(move * speed * Time.deltaTime);

        if(Input.GetButtonDown("Jump") && isGrounded)
        {
            velocity.y = Mathf.Sqrt(jumpHeight * -2f * gravity);
        }

        velocity.y += gravity * Time.deltaTime;

        controller.Move(velocity * Time.deltaTime);

        anima.SetFloat("Forward", z);
        anima.SetFloat("Right", x);
    }
}
