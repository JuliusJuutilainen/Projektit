using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Animations.Rigging;

public class WeaponSwitching : MonoBehaviour
{
    // Start is called before the first frame update

    public TwoBoneIKConstraint leftHand;
    public TwoBoneIKConstraint rightHand;

    public RigBuilder rigBuilder;
    public int selectedWeapon = 0;
    
    void Start()
    {
        SelectedWeapon();
        
    }

    // Update is called once per frame
    void Update()
    {
        int previous = selectedWeapon;

        if(Input.GetAxis("Mouse ScrollWheel") >0f)
        {
            if(selectedWeapon >= 2){
                selectedWeapon = 2;
            }
            selectedWeapon++;

        }

        if(Input.GetAxis("Mouse ScrollWheel") <0f)
        {
            if(selectedWeapon <= 1){
                selectedWeapon = 1;
            }
            selectedWeapon--;

        }

        if(previous != selectedWeapon){
            SelectedWeapon();
        }
        
    }

    void SelectedWeapon(){

        int i = 0;
        foreach(Transform weapon in transform) 
        {      
            //Debug.Log("Weaponswitcher at " + weapon.gameObject.name);

            
            if(i == selectedWeapon)
            {   
                
                weapon.gameObject.SetActive(true);
                weapon.GetComponent<PickUpController>().rigSet(true);
                //Debug.Log(weapon.gameObject.name + " is active or is "+weapon.gameObject.activeSelf);

            }

            else
            {               
                weapon.gameObject.SetActive(false);
                //weapon.GetComponent<PickUpController>().rigSet(false);
                //Debug.Log(weapon.gameObject.name + " is inactive or is "+weapon.gameObject.activeSelf);
            }

            i++;

        }
    }
}
