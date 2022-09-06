using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Animations.Rigging;

public class PickUpController : MonoBehaviour
{
    //public WeaponScript weaponScript;
    
    public TwoBoneIKConstraint leftHand;
    public TwoBoneIKConstraint rightHand;

    public RigBuilder rigBuilder;

    public Transform rHandTarget;
    public Transform lHandTarget;
    
    public NEWWeaponScript weaponScript;
    
    public Rigidbody rb;

    public BoxCollider coll;
    public Transform player, WeaponSlot, cam;

    public float pickupRange;
    public float dropDownwardForce;
    public float dropUpwardForce;

    public bool equipped;
    public bool active;

    public bool droppable = true;
    //private bool actionStatus = false;
    
    //static means var is  same in all scripts of same type
    public static bool slotFull;
    public static int slotAmount;

    

    void Start()
    {
        slotAmount = 4;
        //Setup
        if (!equipped){
            weaponScript.enabled = false;
            rb.isKinematic = false;
            coll.isTrigger = false;

            leftHand.data.target = null;
            rightHand.data.target = null;
            rigBuilder.Build();

            //slotAmount++;
            if(slotAmount>=1){
            slotFull = false;
            }
        }

        if (equipped){
            weaponScript.enabled = true;
            rb.isKinematic = true;
            coll.isTrigger = true;
            slotAmount--;

            leftHand.data.target = lHandTarget;
            rightHand.data.target = rHandTarget;
            rigBuilder.Build();

            if(slotAmount<=0){
                slotFull = true;
            }
            
            
        }
    }

    
    void Update()
    {
        Vector3 distanceToPlayer = player.position - transform.position;
        if(!equipped && distanceToPlayer.magnitude <= pickupRange && Input.GetKeyDown(KeyCode.F) && !slotFull) PickUp();

        //drop w Q
        if(equipped && Input.GetKeyDown(KeyCode.Q) && active && droppable) Drop();

        //Uno reverse card??? why???
        if(!gameObject.activeSelf){
            active = false;
            
        }
        else{
            active = true;
            
        }

        
    }
    private void PickUp(){

        leftHand.data.target = lHandTarget;
        rightHand.data.target = rHandTarget;
        rigBuilder.Build();

        //actionStatus = true;
        slotAmount--;
        equipped = true;

        if(slotAmount<=0){
                slotFull = true;
            }

        weaponScript.enabled = true;
        rb.isKinematic = true;
        coll.isTrigger = true;
        //Make weapon a child of the camera and move to default position
        transform.SetParent(WeaponSlot);
        transform.localPosition = Vector3.zero;
        transform.localRotation = Quaternion.Euler(Vector3.zero);
        //transform.localScale = Vector3.one;
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //1s delay between pickups if needed.
        /*Invoke("StatusReset", 1f);*/

    }

    /*private void StatusReset(){
        actionStatus = false;
    }*/
    private void Drop(){

        leftHand.data.target = null;
        rightHand.data.target = null;
        rigBuilder.Build();

        slotAmount++;

        equipped = false;
        if(slotAmount>=1){
            slotFull = false;
        }
        weaponScript.enabled = false;
        rb.isKinematic = false;
        coll.isTrigger = false;

        transform.SetParent(null);
        

        //Gun moves
        rb.velocity = player.GetComponent<Rigidbody>().velocity;

        //Forces
        rb.AddForce(WeaponSlot.forward * dropDownwardForce, ForceMode.Impulse);
        rb.AddForce(-WeaponSlot.up * dropUpwardForce, ForceMode.Impulse);
        //rotate the flyng gun

        float random = Random.Range(-1f,1f);
        rb.AddTorque(new Vector3(random,random,random) * 10);
    }

    public void rigSet(bool status)
    {   
        if(status){
            leftHand.data.target = lHandTarget;
            rightHand.data.target = rHandTarget;
            rigBuilder.Build();
        }
        else{
            leftHand.data.target = null;
            rightHand.data.target = null;
            rigBuilder.Build();
        }
    }
    // Start is called before the first frame update
    
    public int freeSlots(){
        return slotAmount;
    }

    // Update is called once per frame
    
}
